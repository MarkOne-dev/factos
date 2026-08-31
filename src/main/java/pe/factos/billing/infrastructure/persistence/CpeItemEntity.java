package pe.factos.billing.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cpe_items")
@Getter
@Setter
public class CpeItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "issuer_ruc", referencedColumnName = "issuer_ruc"),
        @JoinColumn(name = "series", referencedColumnName = "series"),
        @JoinColumn(name = "correlative", referencedColumnName = "correlative")
    })
    private CpeEntity cpe;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "quantity", precision = 12, scale = 4, nullable = false)
    private BigDecimal quantity;

    @Column(name = "unit_value", precision = 12, scale = 2, nullable = false)
    private BigDecimal unitValue;

    @Column(name = "unit_price", precision = 12, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "affectation_code", length = 2, nullable = false)
    private String affectationCode;

    @Column(name = "taxable_base", precision = 12, scale = 2, nullable = false)
    private BigDecimal taxableBase;

    @Column(name = "igv", precision = 12, scale = 2, nullable = false)
    private BigDecimal igv;

    @Column(name = "total", precision = 12, scale = 2, nullable = false)
    private BigDecimal total;
}
