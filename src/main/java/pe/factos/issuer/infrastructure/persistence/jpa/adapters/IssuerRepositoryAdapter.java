package pe.factos.issuer.infrastructure.persistence.jpa.adapters;

import org.springframework.stereotype.Repository;
import pe.factos.issuer.domain.model.aggregates.Issuer;
import pe.factos.issuer.domain.model.valueobjects.Ruc;
import pe.factos.issuer.domain.repositories.IssuerRepository;
import pe.factos.issuer.infrastructure.persistence.jpa.entities.IssuerJpaEntity;
import pe.factos.issuer.infrastructure.persistence.jpa.repositories.SpringDataIssuerRepository;

import java.util.Optional;

@Repository
public class IssuerRepositoryAdapter implements IssuerRepository {
    private final SpringDataIssuerRepository repository;

    public IssuerRepositoryAdapter(SpringDataIssuerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Issuer save(Issuer issuer) {
        IssuerJpaEntity entity = repository.findByRuc(issuer.getRuc().value())
                .map(existing -> {
                    existing.setCorporateName(issuer.getCorporateName());
                    existing.setAddress(issuer.getAddress());
                    existing.setUbigeo(issuer.getUbigeo());
                    return existing;
                })
                .orElseGet(() -> new IssuerJpaEntity(issuer));
        return repository.save(entity).toDomain();
    }

    @Override
    public Optional<Issuer> findByRuc(Ruc ruc) {
        return repository.findByRuc(ruc.value())
                .map(IssuerJpaEntity::toDomain);
    }

    @Override
    public boolean existsByRuc(Ruc ruc) {
        return repository.existsByRuc(ruc.value());
    }
}
