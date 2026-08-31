package pe.factos.rendering.application.internal.commandservices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.factos.billing.application.internal.queryservices.CpeQueryService;
import pe.factos.billing.domain.model.queries.GetCpeBySeriesAndCorrelativeQuery;
import pe.factos.rendering.domain.model.commands.GenerateQrCommand;
import pe.factos.rendering.domain.model.commands.RenderPdfCommand;
import pe.factos.rendering.domain.port.DocumentRenderer;
import pe.factos.rendering.domain.port.ObjectStoragePort;
import pe.factos.shared.application.result.ApplicationError;
import pe.factos.shared.application.result.Result;

@Service
public class RenderDocumentCommandServiceImpl implements RenderDocumentCommandService {
    private final CpeQueryService cpeQueryService;
    private final DocumentRenderer documentRenderer;
    private final ObjectStoragePort objectStoragePort;

    @Value("${application.storage.s3.bucket-name:factos-bucket}")
    private String bucketName;

    public RenderDocumentCommandServiceImpl(
            CpeQueryService cpeQueryService,
            DocumentRenderer documentRenderer,
            ObjectStoragePort objectStoragePort
    ) {
        this.cpeQueryService = cpeQueryService;
        this.documentRenderer = documentRenderer;
        this.objectStoragePort = objectStoragePort;
    }

    @Override
    public Result<byte[], ApplicationError> handle(RenderPdfCommand command) {
        var query = new GetCpeBySeriesAndCorrelativeQuery(command.series(), command.correlative());
        var cpeOpt = cpeQueryService.handle(query);
        if (cpeOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("cpe", command.series() + "-" + command.correlative()));
        }

        String objectKey = "facturas/" + command.series() + "-" + command.correlative() + ".pdf";

        // Try downloading existing PDF from Cloudflare R2 bucket first
        try {
            byte[] cachedBytes = objectStoragePort.downloadFile(bucketName, objectKey);
            if (cachedBytes != null && cachedBytes.length > 0) {
                return Result.success(cachedBytes);
            }
        } catch (Exception ignored) {
            // If not found in Cloudflare R2, fallback to render and upload
        }

        try {
            byte[] pdfBytes = documentRenderer.renderPdf(cpeOpt.get());

            // Persist generated PDF to Cloudflare R2 Object Storage
            objectStoragePort.uploadFile(bucketName, objectKey, pdfBytes, "application/pdf");

            return Result.success(pdfBytes);
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("rendering", "Failed to render and store PDF: " + e.getMessage()));
        }
    }

    @Override
    public Result<byte[], ApplicationError> handle(GenerateQrCommand command) {
        try {
            byte[] qrBytes = documentRenderer.generateQr(command.content(), command.width(), command.height());
            return Result.success(qrBytes);
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("rendering", "Failed to generate QR: " + e.getMessage()));
        }
    }
}
