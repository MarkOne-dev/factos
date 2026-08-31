package pe.factos.billing.application;

import pe.factos.billing.domain.model.Cpe;
import pe.factos.billing.domain.port.CpeRepository;
import pe.factos.shared.domain.BusinessException;

public class QueryCpeUseCase {
    private final CpeRepository cpeRepository;

    public QueryCpeUseCase(CpeRepository cpeRepository) {
        this.cpeRepository = cpeRepository;
    }

    public Cpe execute(String series, String correlative) {
        if (series == null || series.isBlank() || correlative == null || correlative.isBlank()) {
            throw new BusinessException("Series and correlative must be provided");
        }
        return cpeRepository.findBySeriesAndCorrelative(series, correlative)
                .orElseThrow(() -> new BusinessException("CPE not found: " + series + "-" + correlative));
    }
}
