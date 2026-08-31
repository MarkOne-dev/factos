package pe.factos.issuer.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.factos.issuer.infrastructure.persistence.jpa.entities.IssuerJpaEntity;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataIssuerRepository extends JpaRepository<IssuerJpaEntity, UUID> {
    Optional<IssuerJpaEntity> findByRuc(String ruc);
    boolean existsByRuc(String ruc);
}
