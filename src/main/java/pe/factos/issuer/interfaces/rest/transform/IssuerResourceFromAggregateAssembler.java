package pe.factos.issuer.interfaces.rest.transform;

import pe.factos.issuer.domain.model.aggregates.Issuer;
import pe.factos.issuer.interfaces.rest.resources.IssuerResource;

public final class IssuerResourceFromAggregateAssembler {
    private IssuerResourceFromAggregateAssembler() {
    }

    public static IssuerResource toResourceFromAggregate(Issuer aggregate) {
        return new IssuerResource(
                aggregate.getRuc().value(),
                aggregate.getCorporateName(),
                aggregate.getAddress(),
                aggregate.getUbigeo()
        );
    }
}
