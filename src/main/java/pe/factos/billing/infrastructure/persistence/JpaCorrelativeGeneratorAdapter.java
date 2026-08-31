package pe.factos.billing.infrastructure.persistence;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.factos.billing.domain.port.CorrelativeGenerator;
import pe.factos.issuer.domain.model.Ruc;

import java.util.Optional;

@Component
public class JpaCorrelativeGeneratorAdapter implements CorrelativeGenerator {

    private final SpringDataCpeRepository springDataCpeRepository;

    public JpaCorrelativeGeneratorAdapter(SpringDataCpeRepository springDataCpeRepository) {
        this.springDataCpeRepository = springDataCpeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public String generateNext(Ruc issuerRuc, String series) {
        Optional<String> maxCorrelativeOpt = springDataCpeRepository.findMaxCorrelativeByIssuerRucAndSeries(
                issuerRuc.value(),
                series
        );

        if (maxCorrelativeOpt.isEmpty() || maxCorrelativeOpt.get() == null) {
            return "00000001";
        }

        String maxCorrelative = maxCorrelativeOpt.get();
        int nextValue = Integer.parseInt(maxCorrelative) + 1;
        return String.format("%08d", nextValue);
    }
}
