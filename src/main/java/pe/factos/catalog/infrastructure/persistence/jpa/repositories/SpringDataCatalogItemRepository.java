package pe.factos.catalog.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.factos.catalog.infrastructure.persistence.jpa.entities.CatalogItemJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataCatalogItemRepository extends JpaRepository<CatalogItemJpaEntity, UUID> {
    Optional<CatalogItemJpaEntity> findByCatalogCodeAndItemCode(String catalogCode, String itemCode);
    List<CatalogItemJpaEntity> findAllByCatalogCode(String catalogCode);
}
