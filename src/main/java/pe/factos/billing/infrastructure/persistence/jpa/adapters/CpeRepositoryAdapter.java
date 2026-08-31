package pe.factos.billing.infrastructure.persistence.jpa.adapters;

import org.springframework.stereotype.Repository;
import pe.factos.billing.domain.model.aggregates.Cpe;
import pe.factos.billing.domain.repositories.CpeRepository;
import pe.factos.billing.infrastructure.persistence.jpa.entities.CpeJpaEntity;
import pe.factos.billing.infrastructure.persistence.jpa.repositories.SpringDataCpeRepository;
import pe.factos.issuer.domain.model.valueobjects.Ruc;

import java.util.List;
import java.util.Optional;

@Repository
public class CpeRepositoryAdapter implements CpeRepository {
    private final SpringDataCpeRepository repository;

    public CpeRepositoryAdapter(SpringDataCpeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cpe save(Cpe cpe) {
        CpeJpaEntity entity = repository.findBySeriesAndCorrelative(cpe.getSeries(), cpe.getCorrelative())
                .map(existing -> {
                    existing.setStatus(cpe.getStatus());
                    return existing;
                })
                .orElseGet(() -> new CpeJpaEntity(cpe));
        return repository.save(entity).toDomain();
    }

    @Override
    public Optional<Cpe> findBySeriesAndCorrelative(String series, String correlative) {
        return repository.findBySeriesAndCorrelative(series, correlative)
                .map(CpeJpaEntity::toDomain);
    }

    @Override
    public List<Cpe> findAllByIssuerRuc(Ruc issuerRuc) {
        return repository.findAllByIssuerRuc(issuerRuc.value()).stream()
                .map(CpeJpaEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsBySeriesAndCorrelative(String series, String correlative) {
        return repository.existsBySeriesAndCorrelative(series, correlative);
    }
}
