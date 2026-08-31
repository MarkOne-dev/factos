package pe.factos.catalog.interfaces.rest.transform;

import pe.factos.catalog.domain.model.aggregates.CatalogItem;
import pe.factos.catalog.interfaces.rest.resources.CatalogItemResource;

public final class CatalogItemResourceFromAggregateAssembler {
    private CatalogItemResourceFromAggregateAssembler() {
    }

    public static CatalogItemResource toResourceFromAggregate(CatalogItem aggregate) {
        return new CatalogItemResource(
                aggregate.getCatalogCode(),
                aggregate.getItemCode(),
                aggregate.getDescription(),
                aggregate.isActive()
        );
    }
}
