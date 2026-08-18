package pe.factos.rendering.domain.port;

import pe.factos.billing.domain.model.Cpe;

public interface DocumentRenderer {
    byte[] renderPdf(Cpe cpe);
    byte[] generateQr(String content, int width, int height);
}
