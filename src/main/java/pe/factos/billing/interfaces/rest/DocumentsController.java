package pe.factos.billing.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.factos.billing.application.internal.commandservices.CpeCommandService;
import pe.factos.billing.application.internal.queryservices.CpeQueryService;
import pe.factos.billing.domain.model.queries.GetCpeBySeriesAndCorrelativeQuery;
import pe.factos.billing.domain.model.queries.GetCpesByIssuerRucQuery;
import pe.factos.billing.interfaces.rest.resources.CpeResource;
import pe.factos.billing.interfaces.rest.resources.EmitCpeResource;
import pe.factos.billing.interfaces.rest.transform.CpeResourceFromAggregateAssembler;
import pe.factos.billing.interfaces.rest.transform.EmitCpeCommandFromResourceAssembler;
import pe.factos.shared.interfaces.rest.transform.ResponseEntityAssembler;

@RestController
@RequestMapping(value = {"/api/v1/documents", "/api/v1/comprobantes"}, produces = "application/json")
@Tag(name = "Electronic Documents", description = "Endpoints for emitting and querying Electronic Invoices (Facturas) and Sales Receipts (Boletas)")
public class DocumentsController {
    private final CpeCommandService commandService;
    private final CpeQueryService queryService;

    public DocumentsController(CpeCommandService commandService, CpeQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    @Operation(summary = "Emit electronic document", description = "Emits and registers a new electronic Invoice (Factura) or Sales Receipt (Boleta) calculating taxable base and IGV")
    public ResponseEntity<?> emitCpe(@Valid @RequestBody EmitCpeResource resource) {
        var command = EmitCpeCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                CpeResourceFromAggregateAssembler::toResourceFromAggregate,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{series}/{correlative}")
    @Operation(summary = "Get document by series and correlative", description = "Returns the electronic document matching the specified series and correlative")
    public ResponseEntity<CpeResource> getCpeBySeriesAndCorrelative(
            @PathVariable String series,
            @PathVariable String correlative
    ) {
        var query = new GetCpeBySeriesAndCorrelativeQuery(series, correlative);
        var cpe = queryService.handle(query);
        return cpe.map(aggregate -> ResponseEntity.ok(CpeResourceFromAggregateAssembler.toResourceFromAggregate(aggregate)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/issuer/{ruc}")
    @Operation(summary = "List documents by issuer RUC", description = "Returns paginated electronic documents emitted by an issuer RUC")
    public ResponseEntity<Page<CpeResource>> getCpesByIssuerRuc(
            @PathVariable String ruc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        var query = new GetCpesByIssuerRucQuery(ruc, pageable);
        var cpesPage = queryService.handle(query);
        var resourcePage = cpesPage.map(CpeResourceFromAggregateAssembler::toResourceFromAggregate);
        return ResponseEntity.ok(resourcePage);
    }
}
