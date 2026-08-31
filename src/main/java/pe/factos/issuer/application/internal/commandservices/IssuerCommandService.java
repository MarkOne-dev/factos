package pe.factos.issuer.application.internal.commandservices;

import pe.factos.issuer.domain.model.aggregates.Issuer;
import pe.factos.issuer.domain.model.commands.CreateIssuerCommand;
import pe.factos.shared.application.result.ApplicationError;
import pe.factos.shared.application.result.Result;

public interface IssuerCommandService {
    Result<Issuer, ApplicationError> handle(CreateIssuerCommand command);
}
