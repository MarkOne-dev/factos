package pe.factos.rendering.infrastructure.pdf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pe.factos.TestRenderingApplication;
import pe.factos.billing.domain.model.*;
import pe.factos.catalog.domain.IgvAffectationType;
import pe.factos.issuer.domain.model.Ruc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestRenderingApplication.class)
class ThymeleafDocumentRendererTest {

    @Autowired
    private ThymeleafDocumentRenderer documentRenderer;

    @Test
    void shouldRenderInvoicePdfAndGenerateQr() {
        // Arrange
        Ruc issuerRuc = new Ruc("20123456789");
        Money unitValue = new Money(BigDecimal.valueOf(100.00), "PEN");
        Money unitPrice = new Money(BigDecimal.valueOf(118.00), "PEN");
        Money igv = new Money(BigDecimal.valueOf(18.00), "PEN");
        Money total = new Money(BigDecimal.valueOf(118.00), "PEN");

        Item item = new Item(
                "P001",
                "Producto de Prueba Nro 1",
                BigDecimal.ONE,
                unitValue,
                unitPrice,
                IgvAffectationType.TAXABLE_ONEROUS,
                new Money(BigDecimal.valueOf(100.00), "PEN"),
                igv,
                total
        );

        CpeTotals totals = new CpeTotals(
                new Money(BigDecimal.valueOf(100.00), "PEN"),
                Money.ZERO,
                Money.ZERO,
                igv,
                Money.ZERO,
                total
        );

        Cpe invoice = new Invoice(
                "F001",
                "00000001",
                LocalDate.now(),
                issuerRuc,
                "20987654321",
                "CLIENTE DE PRUEBA S.A.",
                List.of(item),
                totals
        );

        // Act
        byte[] pdfBytes = documentRenderer.renderPdf(invoice);
        byte[] qrBytes = documentRenderer.generateQr("Test-SUNAT-Content", 200, 200);

        // Assert
        assertThat(pdfBytes).isNotEmpty();
        assertThat(qrBytes).isNotEmpty();
        // Verify PDF magic number (%PDF-)
        assertThat(new String(pdfBytes, 0, 4)).isEqualTo("%PDF");
    }
}
