package pe.factos.billing.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CpeResource(
        String series,
        String correlative,
        String cpeType,
        LocalDate issueDate,
        String issuerRuc,
        String acquirerDocument,
        String acquirerName,
        String status,
        BigDecimal totalTaxable,
        BigDecimal totalIgv,
        BigDecimal totalAmount,
        String currency,
        String pdfUrl,
        List<ItemResource> items
) {
}
