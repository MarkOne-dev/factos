package pe.factos.catalog.infrastructure.persistence.jpa.adapters;

import org.springframework.stereotype.Repository;
import pe.factos.catalog.domain.model.aggregates.CatalogItem;
import pe.factos.catalog.domain.repositories.CatalogItemRepository;
import pe.factos.catalog.infrastructure.persistence.jpa.entities.CatalogItemJpaEntity;
import pe.factos.catalog.infrastructure.persistence.jpa.repositories.SpringDataCatalogItemRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class CatalogItemRepositoryAdapter implements CatalogItemRepository {
    private final SpringDataCatalogItemRepository repository;

    public CatalogItemRepositoryAdapter(SpringDataCatalogItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public CatalogItem save(CatalogItem catalogItem) {
        CatalogItemJpaEntity entity = new CatalogItemJpaEntity(catalogItem);
        return repository.save(entity).toDomain();
    }

    @Override
    public Optional<CatalogItem> findByCatalogCodeAndItemCode(String catalogCode, String itemCode) {
        return repository.findByCatalogCodeAndItemCode(catalogCode, itemCode)
                .map(CatalogItemJpaEntity::toDomain);
    }

    @Override
    public List<CatalogItem> findAllByCatalogCode(String catalogCode) {
        return repository.findAllByCatalogCode(catalogCode).stream()
                .map(CatalogItemJpaEntity::toDomain)
                .toList();
    }
}
