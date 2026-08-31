package pe.factos.issuer.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.factos.issuer.application.internal.commandservices.IssuerCommandService;
import pe.factos.issuer.application.internal.queryservices.IssuerQueryService;
import pe.factos.issuer.domain.model.queries.GetIssuerByRucQuery;
import pe.factos.issuer.interfaces.rest.resources.CreateIssuerResource;
import pe.factos.issuer.interfaces.rest.resources.IssuerResource;
import pe.factos.issuer.interfaces.rest.transform.CreateIssuerCommandFromResourceAssembler;
import pe.factos.issuer.interfaces.rest.transform.IssuerResourceFromAggregateAssembler;
import pe.factos.shared.interfaces.rest.transform.ResponseEntityAssembler;

@RestController
@RequestMapping(value = "/api/v1/issuers", produces = "application/json")
@Tag(name = "Emisores", description = "Endpoints para la gestión de datos de empresas emisoras")
public class IssuerController {
    private final IssuerCommandService commandService;
    private final IssuerQueryService queryService;

    public IssuerController(IssuerCommandService commandService, IssuerQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    @Operation(summary = "Registrar emisor", description = "Registra una nueva empresa emisora de comprobantes con su RUC y razón social")
    public ResponseEntity<?> createIssuer(@Valid @RequestBody CreateIssuerResource resource) {
        var command = CreateIssuerCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                IssuerResourceFromAggregateAssembler::toResourceFromAggregate,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{ruc}")
    @Operation(summary = "Obtener emisor por RUC", description = "Retorna los datos de una empresa emisora mediante su número de RUC")
    public ResponseEntity<IssuerResource> getIssuerByRuc(@PathVariable String ruc) {
        var query = new GetIssuerByRucQuery(ruc);
        var issuer = queryService.handle(query);
        return issuer.map(aggregate -> ResponseEntity.ok(IssuerResourceFromAggregateAssembler.toResourceFromAggregate(aggregate)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
