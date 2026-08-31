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
@RequestMapping(value = "/api/v1/comprobantes", produces = "application/json")
@Tag(name = "Comprobantes Electrónicos", description = "Endpoints para la emisión y consulta de Facturas y Boletas Electrónicas")
public class ComprobantesController {
    private final CpeCommandService commandService;
    private final CpeQueryService queryService;

    public ComprobantesController(CpeCommandService commandService, CpeQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    @Operation(summary = "Emitir comprobante electrónico", description = "Emite y registra una nueva Factura o Boleta electrónica calculando bases imponibles e IGV")
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
    @Operation(summary = "Consultar comprobante por serie y correlativo", description = "Retorna el comprobante electrónico correspondiente a la serie y correlativo indicados")
    public ResponseEntity<CpeResource> getCpeBySeriesAndCorrelative(
            @PathVariable String series,
            @PathVariable String correlative
    ) {
        var query = new GetCpeBySeriesAndCorrelativeQuery(series, correlative);
        var cpe = queryService.handle(query);
        return cpe.map(aggregate -> ResponseEntity.ok(CpeResourceFromAggregateAssembler.toResourceFromAggregate(aggregate)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/emisor/{ruc}")
    @Operation(summary = "Listar comprobantes por RUC emisor", description = "Retorna todos los comprobantes emitidos por una empresa emisora mediante su RUC")
    public ResponseEntity<List<CpeResource>> getCpesByIssuerRuc(@PathVariable String ruc) {
        var query = new GetCpesByIssuerRucQuery(ruc);
        var cpes = queryService.handle(query);
        var resources = cpes.stream()
                .map(CpeResourceFromAggregateAssembler::toResourceFromAggregate)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
