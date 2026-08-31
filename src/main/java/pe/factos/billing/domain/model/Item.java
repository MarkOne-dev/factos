package pe.factos.billing.domain.model;

import pe.factos.catalog.domain.IgvAffectationType;
import pe.factos.shared.domain.BusinessException;

import java.math.BigDecimal;

public class Item {
    private final String code;
    private final String description;
    private final BigDecimal quantity;
    private final Money unitValue;
    private final Money unitPrice;
    private final IgvAffectationType affectationType;
    private final Money taxableBase;
    private final Money igv;
    private final Money total;

    public Item(String code, String description, BigDecimal quantity, Money unitValue, Money unitPrice,
                IgvAffectationType affectationType, Money taxableBase, Money igv, Money total) {
        if (code == null || code.isBlank()) {
            throw new BusinessException("Item code cannot be empty");
        }
        if (description == null || description.isBlank()) {
            throw new BusinessException("Item description cannot be empty");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Item quantity must be greater than zero");
        }
        if (unitValue == null || unitPrice == null) {
            throw new BusinessException("Item unit value and price cannot be null");
        }
        if (affectationType == null) {
            throw new BusinessException("Item IGV affectation type cannot be null");
        }
        if (taxableBase == null || igv == null || total == null) {
            throw new BusinessException("Item calculations (taxable base, igv, total) cannot be null");
        }
        this.code = code;
        this.description = description;
        this.quantity = quantity;
        this.unitValue = unitValue;
        this.unitPrice = unitPrice;
        this.affectationType = affectationType;
        this.taxableBase = taxableBase;
        this.igv = igv;
        this.total = total;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Money getUnitValue() {
        return unitValue;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public IgvAffectationType getAffectationType() {
        return affectationType;
    }

    public Money getTaxableBase() {
        return taxableBase;
    }

    public Money getIgv() {
        return igv;
    }

    public Money getTotal() {
        return total;
    }
}
