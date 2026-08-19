package pe.factos.issuer.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataIssuerRepository extends JpaRepository<IssuerEntity, String> {
}
