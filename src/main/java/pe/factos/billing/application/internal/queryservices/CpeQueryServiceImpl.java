package pe.factos.billing.application.internal.queryservices;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.factos.billing.domain.model.aggregates.Cpe;
import pe.factos.billing.domain.model.queries.GetCpeBySeriesAndCorrelativeQuery;
import pe.factos.billing.domain.model.queries.GetCpesByIssuerRucQuery;
import pe.factos.billing.domain.repositories.CpeRepository;
import pe.factos.issuer.domain.model.valueobjects.Ruc;

import java.util.List;
import java.util.Optional;

@Service
public class CpeQueryServiceImpl implements CpeQueryService {
    private final CpeRepository cpeRepository;

    public CpeQueryServiceImpl(CpeRepository cpeRepository) {
        this.cpeRepository = cpeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cpe> handle(GetCpeBySeriesAndCorrelativeQuery query) {
        return cpeRepository.findBySeriesAndCorrelative(query.series(), query.correlative());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cpe> handle(GetCpesByIssuerRucQuery query) {
        Pageable pageable = query.pageable() != null ? query.pageable() : PageRequest.of(0, 20);
        return cpeRepository.findAllByIssuerRuc(new Ruc(query.issuerRuc()), pageable);
    }
}
