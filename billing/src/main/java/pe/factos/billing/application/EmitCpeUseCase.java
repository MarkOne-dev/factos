package pe.factos.billing.application;

import pe.factos.billing.application.dto.EmitCpeCommand;
import pe.factos.billing.application.dto.CpeResponse;
import pe.factos.billing.application.dto.ItemCommand;
import pe.factos.billing.domain.event.CpeAccepted;
import pe.factos.billing.domain.event.CpeEmitted;
import pe.factos.billing.domain.event.CpeRejected;
import pe.factos.billing.domain.model.*;
import pe.factos.billing.domain.port.*;
import pe.factos.billing.domain.service.IgvCalculator;
import pe.factos.catalog.domain.CpeType;
import pe.factos.catalog.domain.IgvAffectationType;
import pe.factos.issuer.domain.model.Issuer;
import pe.factos.issuer.domain.model.Ruc;
import pe.factos.issuer.domain.port.IssuerRepository;
import pe.factos.shared.domain.BusinessException;
import pe.factos.shared.domain.DomainEventPublisher;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class EmitCpeUseCase {
    private final IssuerRepository issuerRepository;
    private final CpeRepository cpeRepository;
    private final FiscalGateway fiscalGateway;
    private final CorrelativeGenerator correlativeGenerator;
    private final StorageGateway storageGateway;
    private final DomainEventPublisher eventPublisher;
    private final IgvCalculator igvCalculator;

    public EmitCpeUseCase(IssuerRepository issuerRepository, CpeRepository cpeRepository,
                          FiscalGateway fiscalGateway, CorrelativeGenerator correlativeGenerator,
                          StorageGateway storageGateway, DomainEventPublisher eventPublisher) {
        this.issuerRepository = issuerRepository;
        this.cpeRepository = cpeRepository;
        this.fiscalGateway = fiscalGateway;
        this.correlativeGenerator = correlativeGenerator;
        this.storageGateway = storageGateway;
        this.eventPublisher = eventPublisher;
        this.igvCalculator = new IgvCalculator();
    }

    public CpeResponse execute(EmitCpeCommand command) {
        Ruc issuerRuc = new Ruc(command.issuerRuc());
        Issuer issuer = issuerRepository.findByRuc(issuerRuc)
                .orElseThrow(() -> new BusinessException("Issuer with RUC " + command.issuerRuc() + " is not registered"));

        if (issuer.getCertificate() == null || issuer.getCertificate().isExpired(Instant.now())) {
            throw new BusinessException("Issuer's digital certificate is missing or expired");
        }

        if (!issuer.isSeriesAuthorized(command.series())) {
            throw new BusinessException("Series " + command.series() + " is not authorized for this issuer");
        }

        String correlative = correlativeGenerator.generateNext(issuerRuc, command.series());

        List<Item> domainItems = new ArrayList<>();
        String currency = Money.DEFAULT_CURRENCY;
        for (ItemCommand itemCmd : command.items()) {
            IgvAffectationType affectationType = IgvAffectationType.findByCode(itemCmd.affectationCode());
            Money unitValue = new Money(itemCmd.unitValue(), currency);
            Item item = igvCalculator.calculateItem(
                    itemCmd.code(),
                    itemCmd.description(),
                    itemCmd.quantity(),
                    unitValue,
                    affectationType
            );
            domainItems.add(item);
        }

        CpeTotals totals = igvCalculator.calculateTotals(domainItems, currency);

        CpeType cpeType = CpeType.findByCode(command.cpeType());
        Cpe cpe = switch (cpeType) {
            case INVOICE -> new Invoice(
                    command.series(),
                    correlative,
                    command.issueDate(),
                    issuerRuc,
                    command.acquirerDocument(),
                    command.acquirerName(),
                    domainItems,
                    totals
            );
            case BILL -> new Bill(
                    command.series(),
                    correlative,
                    command.issueDate(),
                    issuerRuc,
                    command.acquirerDocument(),
                    command.acquirerName(),
                    domainItems,
                    totals
            );
        };

        cpeRepository.save(cpe);
        eventPublisher.publish(new CpeEmitted(cpe.getSeries(), cpe.getCorrelative(), cpe.getIssuerRuc().value()));

        CpeSubmissionResult result = fiscalGateway.submit(cpe);

        if (result.accepted()) {
            storageGateway.storeXml(cpe.getSeries(), cpe.getCorrelative(), result.signedXml());
            storageGateway.storeCdr(cpe.getSeries(), cpe.getCorrelative(), result.cdrZip());

            eventPublisher.publish(new CpeAccepted(
                    cpe.getSeries(),
                    cpe.getCorrelative(),
                    cpe.getIssuerRuc().value(),
                    result.cdrDescription()
            ));

            return new CpeResponse(
                    cpe.getSeries(),
                    cpe.getCorrelative(),
                    true,
                    result.cdrDescription(),
                    null,
                    null
            );
        } else {
            storageGateway.storeXml(cpe.getSeries(), cpe.getCorrelative(), result.signedXml());

            eventPublisher.publish(new CpeRejected(
                    cpe.getSeries(),
                    cpe.getCorrelative(),
                    cpe.getIssuerRuc().value(),
                    result.errorCode(),
                    result.errorMessage()
            ));

            return new CpeResponse(
                    cpe.getSeries(),
                    cpe.getCorrelative(),
                    false,
                    null,
                    result.errorCode(),
                    result.errorMessage()
            );
        }
    }
}
