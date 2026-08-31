package pe.factos.issuer.application.internal.queryservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.factos.issuer.domain.model.aggregates.Issuer;
import pe.factos.issuer.domain.model.queries.GetIssuerByRucQuery;
import pe.factos.issuer.domain.model.valueobjects.Ruc;
import pe.factos.issuer.domain.repositories.IssuerRepository;

import java.util.Optional;

@Service
public class IssuerQueryServiceImpl implements IssuerQueryService {
    private final IssuerRepository issuerRepository;

    public IssuerQueryServiceImpl(IssuerRepository issuerRepository) {
        this.issuerRepository = issuerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Issuer> handle(GetIssuerByRucQuery query) {
        return issuerRepository.findByRuc(new Ruc(query.ruc()));
    }
}
