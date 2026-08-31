package pe.factos.security.interfaces.rest.transform;

import pe.factos.security.domain.model.aggregates.ApiKey;
import pe.factos.security.interfaces.rest.resources.ApiKeyResource;

public final class ApiKeyResourceFromAggregateAssembler {
    private ApiKeyResourceFromAggregateAssembler() {
    }

    public static ApiKeyResource toResourceFromAggregate(ApiKey aggregate) {
        return new ApiKeyResource(
                aggregate.getKeyValue(),
                aggregate.getClientName(),
                aggregate.getExpiresAt(),
                aggregate.isActive()
        );
    }
}
