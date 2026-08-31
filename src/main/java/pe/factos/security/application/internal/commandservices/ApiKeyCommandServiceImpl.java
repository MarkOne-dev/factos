package pe.factos.security.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.factos.security.domain.model.aggregates.ApiKey;
import pe.factos.security.domain.model.commands.CreateApiKeyCommand;
import pe.factos.security.domain.repositories.ApiKeyRepository;
import pe.factos.shared.application.result.ApplicationError;
import pe.factos.shared.application.result.Result;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
        ApiKey apiKey = new ApiKey(command.clientName(), expiresAt);
        ApiKey saved = apiKeyRepository.save(apiKey);
        return Result.success(saved);
    }
}
