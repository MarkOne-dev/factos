package pe.factos.billing.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.factos.billing.domain.model.aggregates.Cpe;
import pe.factos.billing.domain.model.valueobjects.CpeStatus;
import pe.factos.billing.domain.model.valueobjects.CpeTotals;
import pe.factos.billing.domain.model.valueobjects.Money;
import pe.factos.issuer.domain.model.valueobjects.Ruc;
import pe.factos.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comprobantes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"series", "correlative"})
})
@Getter
@Setter
@NoArgsConstructor
public class CpeJpaEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "series", nullable = false, length = 4)
    private String series;

    @Column(name = "correlative", nullable = false, length = 8)
    private String correlative;

    @Column(name = "cpe_type", nullable = false, length = 2)
    private String cpeType;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "issuer_ruc", nullable = false, length = 11)
    private String issuerRuc;

    @Column(name = "acquirer_document", nullable = false)
    private String acquirerDocument;

    @Column(name = "acquirer_name", nullable = false)
    private String acquirerName;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CpeStatus status;

    @Column(name = "total_taxable", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalTaxable;

    @Column(name = "total_igv", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalIgv;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cpe_id")
    private List<CpeItemJpaEntity> items = new ArrayList<>();

    public CpeJpaEntity(Cpe cpe) {
        this.series = cpe.getSeries();
        this.correlative = cpe.getCorrelative();
        this.cpeType = cpe.getCpeType();
        this.issueDate = cpe.getIssueDate();
        this.issuerRuc = cpe.getIssuerRuc().value();
        this.acquirerDocument = cpe.getAcquirerDocument();
        this.acquirerName = cpe.getAcquirerName();
        this.currency = cpe.getTotals().totalAmount().currency();
        this.status = cpe.getStatus();
        this.totalTaxable = cpe.getTotals().totalTaxable().amount();
        this.totalIgv = cpe.getTotals().totalIgv().amount();
        this.totalAmount = cpe.getTotals().totalAmount().amount();
        this.items = cpe.getItems().stream().map(CpeItemJpaEntity::new).toList();
    }

    public Cpe toDomain() {
        var domainItems = items.stream().map(item -> item.toDomain(currency)).toList();
        var totals = new CpeTotals(
                new Money(totalTaxable, currency),
                new Money(BigDecimal.ZERO, currency),
                new Money(BigDecimal.ZERO, currency),
                new Money(totalIgv, currency),
                new Money(BigDecimal.ZERO, currency),
                new Money(totalAmount, currency)
        );
        return new Cpe(
                series,
                correlative,
                cpeType,
                issueDate,
                new Ruc(issuerRuc),
                acquirerDocument,
                acquirerName,
                domainItems,
                totals,
                status
        );
    }
}
