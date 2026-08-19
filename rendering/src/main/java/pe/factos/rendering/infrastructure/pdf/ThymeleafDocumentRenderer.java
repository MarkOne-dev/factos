package pe.factos.rendering.infrastructure.pdf;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pe.factos.billing.domain.model.Cpe;
import pe.factos.rendering.domain.port.DocumentRenderer;
import pe.factos.shared.domain.BusinessException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Component
public class ThymeleafDocumentRenderer implements DocumentRenderer {

    private final TemplateEngine templateEngine;

    public ThymeleafDocumentRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public byte[] renderPdf(Cpe cpe) {
        try {
            // Build official SUNAT QR metadata content string
            String docType = cpe instanceof pe.factos.billing.domain.model.Invoice ? "01" : "03";
            String acquirerDocType = cpe.getAcquirerDocument() != null && cpe.getAcquirerDocument().length() == 8 ? "1" : "6";
            String acquirerDocNum = cpe.getAcquirerDocument() != null ? cpe.getAcquirerDocument() : "";

            String qrContent = String.join("|",
                    cpe.getIssuerRuc().value(),
                    docType,
                    cpe.getSeries(),
                    cpe.getCorrelative(),
                    cpe.getTotals().totalIgv().amount().toString(),
                    cpe.getTotals().totalAmount().amount().toString(),
                    cpe.getIssueDate().toString(),
                    acquirerDocType,
                    acquirerDocNum,
                    "MOCK_SIGNATURE_VALUE_BASE64_ABC123"
            );

            // Generate QR code png image bytes
            byte[] qrImageBytes = generateQr(qrContent, 200, 200);
            String qrBase64 = Base64.getEncoder().encodeToString(qrImageBytes);

            // Populate Thymeleaf Context
            Context context = new Context();
            context.setVariable("cpe", cpe);
            context.setVariable("totals", cpe.getTotals());
            context.setVariable("items", cpe.getItems());
            context.setVariable("isInvoice", cpe instanceof pe.factos.billing.domain.model.Invoice);
            context.setVariable("qrCodeBase64", "data:image/png;base64," + qrBase64);

            // Process HTML template
            String htmlContent = templateEngine.process("cpe-template", context);

            // Convert HTML to PDF using Flying Saucer OpenPDF
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(htmlContent, "/");
                builder.toStream(os);
                builder.run();
                return os.toByteArray();
            }
        } catch (Exception e) {
            throw new BusinessException("Failed to render PDF document: " + e.getMessage());
        }
    }

    @Override
    public byte[] generateQr(String content, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    width,
                    height
            );

            try (ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream()) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
                return pngOutputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new BusinessException("Failed to generate QR code image: " + e.getMessage());
        }
    }
}
