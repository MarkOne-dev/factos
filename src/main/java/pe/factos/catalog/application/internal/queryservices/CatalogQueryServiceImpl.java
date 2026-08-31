package pe.factos.catalog.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.factos.catalog.domain.model.aggregates.CatalogItem;
import pe.factos.catalog.domain.model.queries.GetAllCatalogItemsQuery;
import pe.factos.catalog.domain.model.queries.GetCatalogItemByCodeQuery;
import pe.factos.catalog.domain.repositories.CatalogItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogQueryServiceImpl implements CatalogQueryService {
    private final CatalogItemRepository catalogItemRepository;

    public CatalogQueryServiceImpl(CatalogItemRepository catalogItemRepository) {
        this.catalogItemRepository = catalogItemRepository;
    }

    @Override
    public Optional<CatalogItem> handle(GetCatalogItemByCodeQuery query) {
        return catalogItemRepository.findByCatalogCodeAndItemCode(query.catalogCode(), query.itemCode());
    }

    @Override
    public List<CatalogItem> handle(GetAllCatalogItemsQuery query) {
        return catalogItemRepository.findAllByCatalogCode(query.catalogCode());
    }
}
