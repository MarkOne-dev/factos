package pe.factos.billing.domain.port;

import pe.factos.billing.domain.model.Cpe;

import java.util.Optional;

public interface CpeRepository {
    void save(Cpe cpe);
    Optional<Cpe> findBySeriesAndCorrelative(String series, String correlative);
}
