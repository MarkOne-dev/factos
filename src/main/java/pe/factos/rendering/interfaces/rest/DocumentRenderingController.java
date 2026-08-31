package pe.factos.rendering.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.factos.rendering.application.internal.commandservices.RenderDocumentCommandService;
import pe.factos.rendering.domain.model.commands.GenerateQrCommand;
import pe.factos.rendering.domain.model.commands.RenderPdfCommand;
import pe.factos.shared.interfaces.rest.transform.ErrorResponseAssembler;

@RestController
@RequestMapping(value = "/api/v1/rendering", produces = "application/json")
@Tag(name = "Renderizado de Documentos", description = "Endpoints para la generación gráfica de PDFs y códigos QR de comprobantes")
public class DocumentRenderingController {
    private final RenderDocumentCommandService commandService;

    public DocumentRenderingController(RenderDocumentCommandService commandService) {
        this.commandService = commandService;
    }

    @GetMapping(value = "/pdf/{series}/{correlative}", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Descargar PDF de comprobante", description = "Genera y retorna el documento PDF oficial impreso del comprobante electrónico")
    public ResponseEntity<?> renderPdf(@PathVariable String series, @PathVariable String correlative) {
        var command = new RenderPdfCommand(series, correlative);
        var result = commandService.handle(command);
        if (result.isSuccess()) {
            byte[] pdfBytes = result.success().get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + series + "-" + correlative + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        }
        return ErrorResponseAssembler.toErrorResponseFromApplicationError(result.failure().get());
    }

    @GetMapping(value = "/qr", produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(summary = "Generar código QR", description = "Genera una imagen PNG del código QR SUNAT a partir del texto ingresado")
    public ResponseEntity<?> generateQr(@RequestParam String content) {
        var command = new GenerateQrCommand(content);
        var result = commandService.handle(command);
        if (result.isSuccess()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(result.success().get());
        }
        return ErrorResponseAssembler.toErrorResponseFromApplicationError(result.failure().get());
    }
}
