package pe.factos.security.domain.repositories;

import pe.factos.security.domain.model.aggregates.ApiKey;

import java.util.Optional;

public interface ApiKeyRepository {
    ApiKey save(ApiKey apiKey);
    Optional<ApiKey> findByKeyValue(String keyValue);
    boolean existsByKeyValue(String keyValue);
}
