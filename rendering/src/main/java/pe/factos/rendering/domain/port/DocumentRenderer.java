package pe.factos.rendering.domain.port;

import pe.factos.billing.domain.model.Comprobante;

public interface DocumentRenderer {
    byte[] renderPdf(Comprobante comprobante);
    byte[] generateQr(String content, int width, int height);
}
