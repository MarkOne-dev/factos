package pe.factos.security.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.factos.security.application.internal.commandservices.ApiKeyCommandService;
import pe.factos.security.application.internal.queryservices.ApiKeyQueryService;
import pe.factos.security.domain.model.queries.GetApiKeyByKeyQuery;
import pe.factos.security.interfaces.rest.resources.ApiKeyResource;
import pe.factos.security.interfaces.rest.resources.CreateApiKeyResource;
import pe.factos.security.interfaces.rest.transform.ApiKeyResourceFromAggregateAssembler;
import pe.factos.security.interfaces.rest.transform.CreateApiKeyCommandFromResourceAssembler;
import pe.factos.shared.interfaces.rest.transform.ResponseEntityAssembler;

@RestController
@RequestMapping(value = "/api/v1/api-keys", produces = "application/json")
@Tag(name = "Seguridad / API Keys", description = "Endpoints para la generación y consulta de llaves de acceso API")
public class ApiKeysController {
    private final ApiKeyCommandService commandService;
    private final ApiKeyQueryService queryService;

    public ApiKeysController(ApiKeyCommandService commandService, ApiKeyQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    @Operation(summary = "Generar API Key", description = "Genera una nueva API Key de acceso para un cliente con vigencia opcional en días")
    public ResponseEntity<?> createApiKey(@Valid @RequestBody CreateApiKeyResource resource) {
        var command = CreateApiKeyCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ApiKeyResourceFromAggregateAssembler::toResourceFromAggregate,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{key}")
    @Operation(summary = "Consultar API Key", description = "Verifica los datos y validez de una API Key enviada")
    public ResponseEntity<ApiKeyResource> getApiKey(@PathVariable String key) {
        var query = new GetApiKeyByKeyQuery(key);
        var apiKey = queryService.handle(query);
        return apiKey.map(aggregate -> ResponseEntity.ok(ApiKeyResourceFromAggregateAssembler.toResourceFromAggregate(aggregate)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
