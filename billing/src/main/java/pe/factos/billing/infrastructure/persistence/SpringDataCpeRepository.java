package pe.factos.billing.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataCpeRepository extends JpaRepository<CpeEntity, CpeId> {
    Optional<CpeEntity> findBySeriesAndCorrelative(String series, String correlative);
}
