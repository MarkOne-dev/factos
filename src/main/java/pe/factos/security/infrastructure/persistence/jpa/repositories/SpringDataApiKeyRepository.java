package pe.factos.security.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.factos.security.infrastructure.persistence.jpa.entities.ApiKeyJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataApiKeyRepository extends JpaRepository<ApiKeyJpaEntity, UUID> {
    Optional<ApiKeyJpaEntity> findByKeyValue(String keyValue);
    boolean existsByKeyValue(String keyValue);
}
