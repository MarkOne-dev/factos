package pe.factos.billing.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface SpringDataCpeRepository extends JpaRepository<CpeEntity, CpeId> {
    Optional<CpeEntity> findBySeriesAndCorrelative(String series, String correlative);

    @Query("SELECT MAX(c.correlative) FROM CpeEntity c WHERE c.issuerRuc = :issuerRuc AND c.series = :series")
    Optional<String> findMaxCorrelativeByIssuerRucAndSeries(@Param("issuerRuc") String issuerRuc, @Param("series") String series);
}
