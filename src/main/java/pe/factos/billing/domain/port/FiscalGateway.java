package pe.factos.billing.domain.port;

import pe.factos.billing.domain.model.Cpe;

public interface FiscalGateway {
    CpeSubmissionResult submit(Cpe cpe);
}
