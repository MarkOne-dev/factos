package pe.factos.catalog.domain.repositories;

import pe.factos.catalog.domain.model.aggregates.CatalogItem;

import java.util.List;
import java.util.Optional;

public interface CatalogItemRepository {
    CatalogItem save(CatalogItem catalogItem);
    Optional<CatalogItem> findByCatalogCodeAndItemCode(String catalogCode, String itemCode);
    List<CatalogItem> findAllByCatalogCode(String catalogCode);
}
