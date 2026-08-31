package pe.factos.security.infrastructure.persistence.jpa.adapters;

import org.springframework.stereotype.Repository;
import pe.factos.security.domain.model.aggregates.ApiKey;
import pe.factos.security.domain.repositories.ApiKeyRepository;
import pe.factos.security.infrastructure.persistence.jpa.entities.ApiKeyJpaEntity;
import pe.factos.security.infrastructure.persistence.jpa.repositories.SpringDataApiKeyRepository;

import java.util.Optional;

@Repository
public class ApiKeyRepositoryAdapter implements ApiKeyRepository {
    private final SpringDataApiKeyRepository repository;

    public ApiKeyRepositoryAdapter(SpringDataApiKeyRepository repository) {
        this.repository = repository;
    }

    @Override
    public ApiKey save(ApiKey apiKey) {
        ApiKeyJpaEntity entity = repository.findByKeyValue(apiKey.getKeyValue())
                .map(existing -> {
                    existing.setActive(apiKey.isActive());
                    return existing;
                })
                .orElseGet(() -> new ApiKeyJpaEntity(apiKey));
        return repository.save(entity).toDomain();
    }

    @Override
    public Optional<ApiKey> findByKeyValue(String keyValue) {
        return repository.findByKeyValue(keyValue)
                .map(ApiKeyJpaEntity::toDomain);
    }

    @Override
    public boolean existsByKeyValue(String keyValue) {
        return repository.existsByKeyValue(keyValue);
    }
}
