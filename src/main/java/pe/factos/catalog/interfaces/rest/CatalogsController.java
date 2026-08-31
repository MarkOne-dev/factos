package pe.factos.catalog.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.factos.catalog.application.internal.queryservices.CatalogQueryService;
import pe.factos.catalog.domain.model.queries.GetAllCatalogItemsQuery;
import pe.factos.catalog.domain.model.queries.GetCatalogItemByCodeQuery;
import pe.factos.catalog.interfaces.rest.resources.CatalogItemResource;
import pe.factos.catalog.interfaces.rest.transform.CatalogItemResourceFromAggregateAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/catalogs", produces = "application/json")
@Tag(name = "Catálogos SUNAT", description = "Endpoints para consultar catálogos de comprobantes y códigos SUNAT")
public class CatalogsController {
    private final CatalogQueryService catalogQueryService;

    public CatalogsController(CatalogQueryService catalogQueryService) {
        this.catalogQueryService = catalogQueryService;
    }

    @GetMapping("/{catalogCode}")
    @Operation(summary = "Obtener ítems de un catálogo", description = "Retorna todos los ítems pertenecientes al código de catálogo SUNAT indicado")
    public ResponseEntity<List<CatalogItemResource>> getCatalogItems(@PathVariable String catalogCode) {
        var query = new GetAllCatalogItemsQuery(catalogCode);
        var items = catalogQueryService.handle(query);
        var resources = items.stream()
                .map(CatalogItemResourceFromAggregateAssembler::toResourceFromAggregate)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{catalogCode}/items/{itemCode}")
    @Operation(summary = "Obtener detalle de un ítem de catálogo", description = "Retorna un ítem específico del catálogo SUNAT indicando su código de ítem")
    public ResponseEntity<CatalogItemResource> getCatalogItem(
            @PathVariable String catalogCode,
            @PathVariable String itemCode
    ) {
        var query = new GetCatalogItemByCodeQuery(catalogCode, itemCode);
        var item = catalogQueryService.handle(query);
        return item.map(aggregate -> ResponseEntity.ok(CatalogItemResourceFromAggregateAssembler.toResourceFromAggregate(aggregate)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
