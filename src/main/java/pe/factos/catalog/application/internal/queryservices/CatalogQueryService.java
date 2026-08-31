package pe.factos.catalog.application.internal.queryservices;

import pe.factos.catalog.domain.model.aggregates.CatalogItem;
import pe.factos.catalog.domain.model.queries.GetAllCatalogItemsQuery;
import pe.factos.catalog.domain.model.queries.GetCatalogItemByCodeQuery;

import java.util.List;
import java.util.Optional;

public interface CatalogQueryService {
    Optional<CatalogItem> handle(GetCatalogItemByCodeQuery query);
    List<CatalogItem> handle(GetAllCatalogItemsQuery query);
}
