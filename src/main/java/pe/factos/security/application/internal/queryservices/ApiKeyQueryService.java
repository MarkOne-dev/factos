package pe.factos.security.application.internal.queryservices;

import pe.factos.security.domain.model.aggregates.ApiKey;
import pe.factos.security.domain.model.queries.GetApiKeyByKeyQuery;

import java.util.Optional;

public interface ApiKeyQueryService {
    Optional<ApiKey> handle(GetApiKeyByKeyQuery query);
}
