package pe.factos.billing.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.factos.billing.infrastructure.persistence.jpa.entities.CpeJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataCpeRepository extends JpaRepository<CpeJpaEntity, UUID> {
    Optional<CpeJpaEntity> findBySeriesAndCorrelative(String series, String correlative);
    List<CpeJpaEntity> findAllByIssuerRuc(String issuerRuc);
    boolean existsBySeriesAndCorrelative(String series, String correlative);
}
