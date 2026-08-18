package pe.factos.billing.domain.service;

import org.junit.jupiter.api.Test;
import pe.factos.billing.domain.model.CpeTotals;
import pe.factos.billing.domain.model.Item;
import pe.factos.billing.domain.model.Money;
import pe.factos.catalog.domain.IgvAffectationType;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IgvCalculatorTest {

    private final IgvCalculator calculator = new IgvCalculator();

    @Test
    void shouldCalculateTaxableOnerousItem() {
        Item item = calculator.calculateItem(
                "P001",
                "Laptop Lenovo",
                BigDecimal.valueOf(2),
                Money.of(1000.00),
                IgvAffectationType.TAXABLE_ONEROUS
        );

        assertEquals("P001", item.getCode());
        assertEquals(new BigDecimal("1180.00"), item.getUnitPrice().amount());
        assertEquals(new BigDecimal("2000.00"), item.getTaxableBase().amount());
        assertEquals(new BigDecimal("360.00"), item.getIgv().amount());
        assertEquals(new BigDecimal("2360.00"), item.getTotal().amount());
    }

    @Test
    void shouldCalculateExoneratedOnerousItem() {
        Item item = calculator.calculateItem(
                "P002",
                "Libro de Java",
                BigDecimal.valueOf(1),
                Money.of(50.00),
                IgvAffectationType.EXONERATED_ONEROUS
        );

        assertEquals(new BigDecimal("50.00"), item.getUnitPrice().amount());
        assertEquals(new BigDecimal("50.00"), item.getTaxableBase().amount());
        assertEquals(BigDecimal.ZERO.setScale(2), item.getIgv().amount());
        assertEquals(new BigDecimal("50.00"), item.getTotal().amount());
    }

    @Test
    void shouldCalculateFreeTaxableItem() {
        Item item = calculator.calculateItem(
                "P003",
                "Muestra Gratis",
                BigDecimal.valueOf(5),
                Money.of(10.00),
                IgvAffectationType.TAXABLE_RETIREMENT_PREMIUM
        );

        assertEquals(new BigDecimal("11.80"), item.getUnitPrice().amount());
        assertEquals(new BigDecimal("50.00"), item.getTaxableBase().amount());
        assertEquals(new BigDecimal("9.00"), item.getIgv().amount());
        assertEquals(BigDecimal.ZERO.setScale(2), item.getTotal().amount());
    }

    @Test
    void shouldCalculateInvoiceTotalsCorrectly() {
        Item laptop = calculator.calculateItem(
                "P001", "Laptop", BigDecimal.valueOf(1), Money.of(3000.00), IgvAffectationType.TAXABLE_ONEROUS);
        Item book = calculator.calculateItem(
                "P002", "Book", BigDecimal.valueOf(2), Money.of(50.00), IgvAffectationType.EXONERATED_ONEROUS);
        Item freeSample = calculator.calculateItem(
                "P003", "Gift", BigDecimal.valueOf(1), Money.of(20.00), IgvAffectationType.TAXABLE_RETIREMENT_PREMIUM);

        CpeTotals totals = calculator.calculateTotals(List.of(laptop, book, freeSample), "PEN");

        assertEquals(new BigDecimal("3000.00"), totals.totalTaxable().amount());
        assertEquals(new BigDecimal("100.00"), totals.totalExonerated().amount());
        assertEquals(BigDecimal.ZERO.setScale(2), totals.totalInactive().amount());
        assertEquals(new BigDecimal("540.00"), totals.totalIgv().amount());
        assertEquals(new BigDecimal("23.60"), totals.totalFree().amount());
        assertEquals(new BigDecimal("3640.00"), totals.totalAmount().amount());
    }
}
