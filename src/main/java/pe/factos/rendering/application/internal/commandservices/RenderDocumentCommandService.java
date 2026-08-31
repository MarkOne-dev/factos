package pe.factos.rendering.application.internal.commandservices;

import pe.factos.rendering.domain.model.commands.GenerateQrCommand;
import pe.factos.rendering.domain.model.commands.RenderPdfCommand;
import pe.factos.shared.application.result.ApplicationError;
import pe.factos.shared.application.result.Result;

public interface RenderDocumentCommandService {
    Result<byte[], ApplicationError> handle(RenderPdfCommand command);
    Result<byte[], ApplicationError> handle(GenerateQrCommand command);
}
