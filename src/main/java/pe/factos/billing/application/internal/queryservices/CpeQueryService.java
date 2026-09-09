package pe.factos.billing.application.internal.queryservices;

import org.springframework.data.domain.Page;
import pe.factos.billing.domain.model.aggregates.Cpe;
import pe.factos.billing.domain.model.queries.GetCpeBySeriesAndCorrelativeQuery;
import pe.factos.billing.domain.model.queries.GetCpesByIssuerRucQuery;

import java.util.Optional;

public interface CpeQueryService {
    Optional<Cpe> handle(GetCpeBySeriesAndCorrelativeQuery query);
    Page<Cpe> handle(GetCpesByIssuerRucQuery query);
}
