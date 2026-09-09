package pe.factos.billing.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.factos.billing.domain.model.aggregates.Cpe;
import pe.factos.issuer.domain.model.valueobjects.Ruc;

import java.util.List;
import java.util.Optional;

public interface CpeRepository {
    Cpe save(Cpe cpe);
    Optional<Cpe> findBySeriesAndCorrelative(String series, String correlative);
    List<Cpe> findAllByIssuerRuc(Ruc issuerRuc);
    Page<Cpe> findAllByIssuerRuc(Ruc issuerRuc, Pageable pageable);
    boolean existsBySeriesAndCorrelative(String series, String correlative);
}
