package pe.factos.issuer.interfaces.rest.transform;

import pe.factos.issuer.domain.model.commands.CreateIssuerCommand;
import pe.factos.issuer.interfaces.rest.resources.CreateIssuerResource;

public final class CreateIssuerCommandFromResourceAssembler {
    private CreateIssuerCommandFromResourceAssembler() {
    }

    public static CreateIssuerCommand toCommandFromResource(CreateIssuerResource resource) {
        return new CreateIssuerCommand(
                resource.ruc(),
                resource.corporateName(),
                resource.address(),
                resource.ubigeo()
        );
    }
}
