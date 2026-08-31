package pe.factos.billing.domain.service;

import pe.factos.billing.domain.model.CpeTotals;
import pe.factos.billing.domain.model.Item;
import pe.factos.billing.domain.model.Money;
import pe.factos.catalog.domain.IgvAffectationType;
import pe.factos.shared.domain.BusinessException;

import java.math.BigDecimal;
import java.util.List;

public class IgvCalculator {
    private static final BigDecimal IGV_RATE = new BigDecimal("0.18");

    public Item calculateItem(String code, String description, BigDecimal quantity, Money unitValue, IgvAffectationType affectationType) {
        if (unitValue == null) {
            throw new BusinessException("Unit value cannot be null");
        }
        String currency = unitValue.currency();
        Money unitPrice;
        Money taxableBase;
        Money igv;
        Money total;

        BigDecimal baseAmount = unitValue.amount().multiply(quantity);

        if (affectationType.isTaxable()) {
            if (affectationType.isFree()) {
                unitPrice = unitValue.multiply(BigDecimal.ONE.add(IGV_RATE));
                taxableBase = new Money(baseAmount, currency);
                igv = taxableBase.multiply(IGV_RATE);
                total = new Money(BigDecimal.ZERO, currency);
            } else {
                unitPrice = unitValue.multiply(BigDecimal.ONE.add(IGV_RATE));
                taxableBase = new Money(baseAmount, currency);
                igv = taxableBase.multiply(IGV_RATE);
                total = taxableBase.add(igv);
            }
        } else {
            if (affectationType.isFree()) {
                unitPrice = unitValue;
                taxableBase = new Money(baseAmount, currency);
                igv = new Money(BigDecimal.ZERO, currency);
                total = new Money(BigDecimal.ZERO, currency);
            } else {
                unitPrice = unitValue;
                taxableBase = new Money(baseAmount, currency);
                igv = new Money(BigDecimal.ZERO, currency);
                total = taxableBase;
            }
        }

        return new Item(code, description, quantity, unitValue, unitPrice, affectationType, taxableBase, igv, total);
    }

    public CpeTotals calculateTotals(List<Item> items, String currency) {
        if (items == null || items.isEmpty()) {
            throw new BusinessException("Cannot calculate totals for empty items list");
        }

        Money totalTaxable = new Money(BigDecimal.ZERO, currency);
        Money totalExonerated = new Money(BigDecimal.ZERO, currency);
        Money totalInactive = new Money(BigDecimal.ZERO, currency);
        Money totalIgv = new Money(BigDecimal.ZERO, currency);
        Money totalFree = new Money(BigDecimal.ZERO, currency);
        Money totalAmount = new Money(BigDecimal.ZERO, currency);

        for (Item item : items) {
            IgvAffectationType type = item.getAffectationType();
            if (type.isFree()) {
                BigDecimal referentialAmount = item.getTaxableBase().amount().add(item.getIgv().amount());
                totalFree = totalFree.add(new Money(referentialAmount, currency));
            } else {
                totalIgv = totalIgv.add(item.getIgv());
                totalAmount = totalAmount.add(item.getTotal());

                switch (type) {
                    case TAXABLE_ONEROUS -> totalTaxable = totalTaxable.add(item.getTaxableBase());
                    case EXONERATED_ONEROUS -> totalExonerated = totalExonerated.add(item.getTaxableBase());
                    case INACTIVE_ONEROUS, EXPORT_SERVICES -> totalInactive = totalInactive.add(item.getTaxableBase());
                    default -> throw new BusinessException("Unsupported onerous affectation type: " + type);
                }
            }
        }

        return new CpeTotals(totalTaxable, totalExonerated, totalInactive, totalIgv, totalFree, totalAmount);
    }
}
