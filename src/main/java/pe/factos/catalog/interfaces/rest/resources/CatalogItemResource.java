package pe.factos.catalog.interfaces.rest.resources;

public record CatalogItemResource(
        String catalogCode,
        String itemCode,
        String description,
        boolean active
) {
}
