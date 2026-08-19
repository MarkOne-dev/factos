package pe.factos.billing.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cpes")
@IdClass(CpeId.class)
@Getter
@Setter
public class CpeEntity {
    @Id
    @Column(name = "issuer_ruc", length = 11)
    private String issuerRuc;

    @Id
    @Column(name = "series", length = 4)
    private String series;

    @Id
    @Column(name = "correlative", length = 8)
    private String correlative;

    @Column(name = "cpe_type", length = 2, nullable = false)
    private String cpeType;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "acquirer_document", length = 20)
    private String acquirerDocument;

    @Column(name = "acquirer_name", length = 255)
    private String acquirerName;

    @Column(name = "total_taxable", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalTaxable;

    @Column(name = "total_exonerated", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalExonerated;

    @Column(name = "total_inactive", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalInactive;

    @Column(name = "total_igv", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalIgv;

    @Column(name = "total_free", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalFree;

    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "currency", length = 3, nullable = false)
    private String currency;

    @Column(name = "state", length = 20, nullable = false)
    private String state;

    @Column(name = "cdr_description")
    private String cdrDescription;

    @Column(name = "error_code", length = 10)
    private String errorCode;

    @Column(name = "error_message")
    private String errorMessage;

    @OneToMany(mappedBy = "cpe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CpeItemEntity> items = new ArrayList<>();

    public void addItem(CpeItemEntity item) {
        items.add(item);
        item.setCpe(this);
    }
}
