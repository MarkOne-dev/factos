package pe.factos.billing.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.factos.billing.domain.model.aggregates.Cpe;
import pe.factos.billing.domain.model.commands.EmitCpeCommand;
import pe.factos.billing.domain.model.entities.Item;
import pe.factos.billing.domain.model.valueobjects.CpeTotals;
import pe.factos.billing.domain.model.valueobjects.Money;
import pe.factos.billing.domain.repositories.CpeRepository;
import pe.factos.catalog.domain.IgvAffectationType;
import pe.factos.issuer.domain.model.valueobjects.Ruc;
import pe.factos.issuer.domain.repositories.IssuerRepository;
import pe.factos.shared.application.result.ApplicationError;
import pe.factos.shared.application.result.Result;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CpeCommandServiceImpl implements CpeCommandService {
    private final CpeRepository cpeRepository;
    private final IssuerRepository issuerRepository;

    public CpeCommandServiceImpl(CpeRepository cpeRepository, IssuerRepository issuerRepository) {
        this.cpeRepository = cpeRepository;
        this.issuerRepository = issuerRepository;
    }

    @Override
    @Transactional
    public Result<Cpe, ApplicationError> handle(EmitCpeCommand command) {
        var issuerRuc = new Ruc(command.issuerRuc());
        if (!issuerRepository.existsByRuc(issuerRuc)) {
            return Result.failure(ApplicationError.notFound("issuer", command.issuerRuc()));
        }

        if (cpeRepository.existsBySeriesAndCorrelative(command.series(), command.correlative())) {
            return Result.failure(ApplicationError.conflict("cpe", command.series() + "-" + command.correlative()));
        }

        String currency = command.currency() != null ? command.currency() : "PEN";
        List<Item> domainItems = new ArrayList<>();
        BigDecimal totalTaxable = BigDecimal.ZERO;
        BigDecimal totalIgv = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var itemCmd : command.items()) {
            BigDecimal unitPrice = itemCmd.unitPrice();
            BigDecimal quantity = itemCmd.quantity();
            BigDecimal itemTotal = unitPrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
            BigDecimal itemTaxableBase = itemTotal.divide(BigDecimal.valueOf(1.18), 2, RoundingMode.HALF_UP);
            BigDecimal itemIgv = itemTotal.subtract(itemTaxableBase);
            BigDecimal unitValue = unitPrice.divide(BigDecimal.valueOf(1.18), 4, RoundingMode.HALF_UP);

            totalTaxable = totalTaxable.add(itemTaxableBase);
            totalIgv = totalIgv.add(itemIgv);
            totalAmount = totalAmount.add(itemTotal);

            domainItems.add(new Item(
                    itemCmd.code(),
                    itemCmd.description(),
                    quantity,
                    Money.of(unitValue),
                    Money.of(unitPrice),
                    IgvAffectationType.TAXABLE_ONEROUS,
                    Money.of(itemTaxableBase),
                    Money.of(itemIgv),
                    Money.of(itemTotal)
            ));
        }

        CpeTotals totals = new CpeTotals(
                new Money(totalTaxable, currency),
                new Money(BigDecimal.ZERO, currency),
                new Money(BigDecimal.ZERO, currency),
                new Money(totalIgv, currency),
                new Money(BigDecimal.ZERO, currency),
                new Money(totalAmount, currency)
        );

        Cpe cpe = new Cpe(
                command.series(),
                command.correlative(),
                command.cpeType(),
                command.issueDate(),
                issuerRuc,
                command.acquirerDocument(),
                command.acquirerName(),
                domainItems,
                totals
        );

        Cpe savedCpe = cpeRepository.save(cpe);
        return Result.success(savedCpe);
    }
}
