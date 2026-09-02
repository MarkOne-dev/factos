package pe.factos.billing.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

import java.util.List;

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
    @Operation(summary = "List documents by issuer RUC", description = "Returns all electronic documents emitted by an issuer RUC")
    public ResponseEntity<List<CpeResource>> getCpesByIssuerRuc(@PathVariable String ruc) {
        var query = new GetCpesByIssuerRucQuery(ruc);
        var cpes = queryService.handle(query);
        var resources = cpes.stream()
                .map(CpeResourceFromAggregateAssembler::toResourceFromAggregate)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
