package pe.factos.billing.domain.model;

public record CpeTotals(
        Money totalTaxable,
        Money totalExonerated,
        Money totalInactive,
        Money totalIgv,
        Money totalFree,
        Money totalAmount
) {
    public static CpeTotals empty(String currency) {
        Money zero = new Money(java.math.BigDecimal.ZERO, currency);
        return new CpeTotals(zero, zero, zero, zero, zero, zero);
    }
}
