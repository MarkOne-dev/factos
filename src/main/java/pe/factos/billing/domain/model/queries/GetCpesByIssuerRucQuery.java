package pe.factos.billing.domain.model.queries;

import org.springframework.data.domain.Pageable;

public record GetCpesByIssuerRucQuery(String issuerRuc, Pageable pageable) {
    public GetCpesByIssuerRucQuery(String issuerRuc) {
        this(issuerRuc, null);
    }
}
