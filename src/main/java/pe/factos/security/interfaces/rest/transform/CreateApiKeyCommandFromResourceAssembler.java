package pe.factos.security.interfaces.rest.transform;

import pe.factos.security.domain.model.commands.CreateApiKeyCommand;
import pe.factos.security.interfaces.rest.resources.CreateApiKeyResource;

public final class CreateApiKeyCommandFromResourceAssembler {
    private CreateApiKeyCommandFromResourceAssembler() {
    }

    public static CreateApiKeyCommand toCommandFromResource(CreateApiKeyResource resource) {
        return new CreateApiKeyCommand(resource.clientName(), resource.validDays());
    }
}
