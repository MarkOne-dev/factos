package pe.factos.rendering.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.factos.billing.application.internal.queryservices.CpeQueryService;
import pe.factos.billing.domain.model.queries.GetCpeBySeriesAndCorrelativeQuery;
import pe.factos.rendering.domain.model.commands.GenerateQrCommand;
import pe.factos.rendering.domain.model.commands.RenderPdfCommand;
import pe.factos.rendering.domain.port.DocumentRenderer;
import pe.factos.shared.application.result.ApplicationError;
import pe.factos.shared.application.result.Result;

@Service
public class RenderDocumentCommandServiceImpl implements RenderDocumentCommandService {
    private final CpeQueryService cpeQueryService;
    private final DocumentRenderer documentRenderer;

    public RenderDocumentCommandServiceImpl(CpeQueryService cpeQueryService, DocumentRenderer documentRenderer) {
        this.cpeQueryService = cpeQueryService;
        this.documentRenderer = documentRenderer;
    }

    @Override
    public Result<byte[], ApplicationError> handle(RenderPdfCommand command) {
        var query = new GetCpeBySeriesAndCorrelativeQuery(command.series(), command.correlative());
        var cpeOpt = cpeQueryService.handle(query);
        if (cpeOpt.isEmpty()) {
            return Result.failure(ApplicationError.notFound("cpe", command.series() + "-" + command.correlative()));
        }

        try {
            byte[] pdfBytes = documentRenderer.renderPdf(cpeOpt.get());
            return Result.success(pdfBytes);
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("rendering", "Failed to render PDF: " + e.getMessage()));
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
