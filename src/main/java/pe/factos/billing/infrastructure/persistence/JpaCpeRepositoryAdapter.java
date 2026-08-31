package pe.factos.billing.infrastructure.persistence;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.factos.billing.domain.model.*;
import pe.factos.billing.domain.port.CpeRepository;
import pe.factos.catalog.domain.IgvAffectationType;
import pe.factos.issuer.domain.model.valueobjects.Ruc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JpaCpeRepositoryAdapter implements CpeRepository {
    private final SpringDataCpeRepository springDataRepository;

    public JpaCpeRepositoryAdapter(SpringDataCpeRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    @Transactional
    public void save(Cpe cpe) {
        CpeEntity entity = toEntity(cpe);
        springDataRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cpe> findBySeriesAndCorrelative(String series, String correlative) {
        return springDataRepository.findBySeriesAndCorrelative(series, correlative)
                .map(this::toDomain);
    }

    private CpeEntity toEntity(Cpe domain) {
        CpeEntity entity = new CpeEntity();
        entity.setIssuerRuc(domain.getIssuerRuc().value());
        entity.setSeries(domain.getSeries());
        entity.setCorrelative(domain.getCorrelative());
        
        String cpeType = (domain instanceof Invoice) ? "01" : "03";
        entity.setCpeType(cpeType);
        entity.setIssueDate(domain.getIssueDate());
        entity.setAcquirerDocument(domain.getAcquirerDocument());
        entity.setAcquirerName(domain.getAcquirerName());

        CpeTotals totals = domain.getTotals();
        entity.setTotalTaxable(totals.totalTaxable().amount());
        entity.setTotalExonerated(totals.totalExonerated().amount());
        entity.setTotalInactive(totals.totalInactive().amount());
        entity.setTotalIgv(totals.totalIgv().amount());
        entity.setTotalFree(totals.totalFree().amount());
        entity.setTotalAmount(totals.totalAmount().amount());
        entity.setCurrency(totals.totalAmount().currency());
        entity.setState("PENDING"); 

        for (Item item : domain.getItems()) {
            CpeItemEntity itemEntity = new CpeItemEntity();
            itemEntity.setCode(item.getCode());
            itemEntity.setDescription(item.getDescription());
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setUnitValue(item.getUnitValue().amount());
            itemEntity.setUnitPrice(item.getUnitPrice().amount());
            itemEntity.setAffectationCode(item.getAffectationType().getCode());
            itemEntity.setTaxableBase(item.getTaxableBase().amount());
            itemEntity.setIgv(item.getIgv().amount());
            itemEntity.setTotal(item.getTotal().amount());
            
            entity.addItem(itemEntity);
        }

        return entity;
    }

    private Cpe toDomain(CpeEntity entity) {
        String currency = entity.getCurrency();
        List<Item> domainItems = entity.getItems().stream()
                .map(itemEntity -> new Item(
                        itemEntity.getCode(),
                        itemEntity.getDescription(),
                        itemEntity.getQuantity(),
                        new Money(itemEntity.getUnitValue(), currency),
                        new Money(itemEntity.getUnitPrice(), currency),
                        IgvAffectationType.findByCode(itemEntity.getAffectationCode()),
                        new Money(itemEntity.getTaxableBase(), currency),
                        new Money(itemEntity.getIgv(), currency),
                        new Money(itemEntity.getTotal(), currency)
                ))
                .collect(Collectors.toList());

        CpeTotals totals = new CpeTotals(
                new Money(entity.getTotalTaxable(), currency),
                new Money(entity.getTotalExonerated(), currency),
                new Money(entity.getTotalInactive(), currency),
                new Money(entity.getTotalIgv(), currency),
                new Money(entity.getTotalFree(), currency),
                new Money(entity.getTotalAmount(), currency)
        );

        Ruc issuerRuc = new Ruc(entity.getIssuerRuc());

        if ("01".equals(entity.getCpeType())) {
            return new Invoice(
                    entity.getSeries(),
                    entity.getCorrelative(),
                    entity.getIssueDate(),
                    issuerRuc,
                    entity.getAcquirerDocument(),
                    entity.getAcquirerName(),
                    domainItems,
                    totals
            );
        } else {
            return new Bill(
                    entity.getSeries(),
                    entity.getCorrelative(),
                    entity.getIssueDate(),
                    issuerRuc,
                    entity.getAcquirerDocument(),
                    entity.getAcquirerName(),
                    domainItems,
                    totals
            );
        }
    }
}
