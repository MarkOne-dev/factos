package pe.factos.security.interfaces.rest.transform;

import pe.factos.security.domain.model.aggregates.ApiKey;
import pe.factos.security.interfaces.rest.resources.ApiKeyResource;

public final class ApiKeyResourceFromAggregateAssembler {
    private ApiKeyResourceFromAggregateAssembler() {
    }

    public static ApiKeyResource toResourceFromAggregate(ApiKey aggregate) {
        String keyToDisplay = aggregate.getRawKey() != null ? aggregate.getRawKey() : aggregate.getKeyValue();
        return new ApiKeyResource(
                keyToDisplay,
                aggregate.getClientName(),
                aggregate.getExpiresAt(),
                aggregate.isActive()
        );
    }
}
