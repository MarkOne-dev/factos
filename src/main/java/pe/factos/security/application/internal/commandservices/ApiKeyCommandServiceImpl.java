package pe.factos.security.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.factos.security.domain.model.aggregates.ApiKey;
import pe.factos.security.domain.model.commands.CreateApiKeyCommand;
import pe.factos.security.domain.repositories.ApiKeyRepository;
import pe.factos.shared.application.result.ApplicationError;
import pe.factos.shared.application.result.Result;

import pe.factos.security.infrastructure.security.ApiKeyHashUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class ApiKeyCommandServiceImpl implements ApiKeyCommandService {
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyCommandServiceImpl(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    @Transactional
    public Result<ApiKey, ApplicationError> handle(CreateApiKeyCommand command) {
        Instant expiresAt = command.validDays() != null && command.validDays() > 0
                ? Instant.now().plus(command.validDays(), ChronoUnit.DAYS)
                : null;
        
        String rawKey = "fct_" + UUID.randomUUID().toString().replace("-", "");
        String hashedKey = ApiKeyHashUtils.hashApiKey(rawKey);

        ApiKey apiKey = new ApiKey(hashedKey, rawKey, command.clientName(), expiresAt, true);
        ApiKey saved = apiKeyRepository.save(apiKey);
        
        // Return ApiKey preserving rawKey for initial response presentation
        ApiKey resultApiKey = new ApiKey(saved.getKeyValue(), rawKey, saved.getClientName(), saved.getExpiresAt(), saved.isActive());
        return Result.success(resultApiKey);
    }
}
