package pe.factos.billing.domain.port;

import pe.factos.issuer.domain.model.Ruc;

public interface CorrelativeGenerator {
    String generateNext(Ruc issuerRuc, String series);
}
