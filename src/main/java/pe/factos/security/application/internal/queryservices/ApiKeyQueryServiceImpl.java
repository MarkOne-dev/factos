package pe.factos.security.application.internal.queryservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.factos.security.domain.model.aggregates.ApiKey;
import pe.factos.security.domain.model.queries.GetApiKeyByKeyQuery;
import pe.factos.security.domain.repositories.ApiKeyRepository;

import java.util.Optional;

@Service
public class ApiKeyQueryServiceImpl implements ApiKeyQueryService {
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyQueryServiceImpl(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApiKey> handle(GetApiKeyByKeyQuery query) {
        return apiKeyRepository.findByKeyValue(query.keyValue());
    }
}
