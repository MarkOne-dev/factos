package pe.factos.billing.application.internal.commandservices;

import pe.factos.billing.domain.model.aggregates.Cpe;
import pe.factos.billing.domain.model.commands.EmitCpeCommand;
import pe.factos.shared.application.result.ApplicationError;
import pe.factos.shared.application.result.Result;

public interface CpeCommandService {
    Result<Cpe, ApplicationError> handle(EmitCpeCommand command);
}
