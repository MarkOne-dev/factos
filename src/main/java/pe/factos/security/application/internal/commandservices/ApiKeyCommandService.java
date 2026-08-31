package pe.factos.security.application.internal.commandservices;

import pe.factos.security.domain.model.aggregates.ApiKey;
import pe.factos.security.domain.model.commands.CreateApiKeyCommand;
import pe.factos.shared.application.result.ApplicationError;
import pe.factos.shared.application.result.Result;

public interface ApiKeyCommandService {
    Result<ApiKey, ApplicationError> handle(CreateApiKeyCommand command);
}
