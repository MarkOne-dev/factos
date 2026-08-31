package pe.factos.billing.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.factos.billing.domain.model.entities.Item;
import pe.factos.billing.domain.model.valueobjects.Money;
import pe.factos.catalog.domain.IgvAffectationType;
import pe.factos.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "cpe_items")
@Getter
@Setter
@NoArgsConstructor
public class CpeItemJpaEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "quantity", nullable = false, precision = 12, scale = 4)
    private BigDecimal quantity;

    @Column(name = "unit_value", nullable = false, precision = 12, scale = 4)
    private BigDecimal unitValue;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "affectation_type", nullable = false)
    private String affectationType;

    @Column(name = "taxable_base", nullable = false, precision = 12, scale = 2)
    private BigDecimal taxableBase;

    @Column(name = "igv", nullable = false, precision = 12, scale = 2)
    private BigDecimal igv;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    public CpeItemJpaEntity(Item item) {
        this.code = item.getCode();
        this.description = item.getDescription();
        this.quantity = item.getQuantity();
        this.unitValue = item.getUnitValue().amount();
        this.unitPrice = item.getUnitPrice().amount();
        this.affectationType = item.getAffectationType().name();
        this.taxableBase = item.getTaxableBase().amount();
        this.igv = item.getIgv().amount();
        this.total = item.getTotal().amount();
    }

    public Item toDomain(String currency) {
        return new Item(
                code,
                description,
                quantity,
                new Money(unitValue, currency),
                new Money(unitPrice, currency),
                IgvAffectationType.valueOf(affectationType),
                new Money(taxableBase, currency),
                new Money(igv, currency),
                new Money(total, currency)
        );
    }
}
