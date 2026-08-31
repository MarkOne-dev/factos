package pe.factos.catalog.domain.model.aggregates;

import lombok.Getter;
import pe.factos.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

/**
 * Aggregate Root for SUNAT catalog items (e.g. Catalog 01: CPE Types, Catalog 06: Document Types, etc.)
 */
@Getter
public class CatalogItem extends AbstractDomainAggregateRoot<CatalogItem> {
    private final String catalogCode;
    private final String itemCode;
    private final String description;
    private final boolean active;

    public CatalogItem(String catalogCode, String itemCode, String description, boolean active) {
        this.catalogCode = catalogCode;
        this.itemCode = itemCode;
        this.description = description;
        this.active = active;
    }

    public CatalogItem(String catalogCode, String itemCode, String description) {
        this(catalogCode, itemCode, description, true);
    }
}
