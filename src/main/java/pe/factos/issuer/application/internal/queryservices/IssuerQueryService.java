package pe.factos.issuer.application.internal.queryservices;

import pe.factos.issuer.domain.model.aggregates.Issuer;
import pe.factos.issuer.domain.model.queries.GetIssuerByRucQuery;

import java.util.Optional;

public interface IssuerQueryService {
    Optional<Issuer> handle(GetIssuerByRucQuery query);
}
