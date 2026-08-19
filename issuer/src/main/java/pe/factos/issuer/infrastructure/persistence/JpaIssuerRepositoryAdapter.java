package pe.factos.issuer.infrastructure.persistence;

import org.springframework.stereotype.Component;
import pe.factos.issuer.domain.model.AuthorizedSeries;
import pe.factos.issuer.domain.model.DigitalCertificate;
import pe.factos.issuer.domain.model.Issuer;
import pe.factos.issuer.domain.model.Ruc;
import pe.factos.issuer.domain.port.IssuerRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JpaIssuerRepositoryAdapter implements IssuerRepository {
    private final SpringDataIssuerRepository springDataRepository;

    public JpaIssuerRepositoryAdapter(SpringDataIssuerRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public void save(Issuer issuer) {
        IssuerEntity entity = toEntity(issuer);
        springDataRepository.save(entity);
    }

    @Override
    public Optional<Issuer> findByRuc(Ruc ruc) {
        return springDataRepository.findById(ruc.value())
                .map(this::toDomain);
    }

    private IssuerEntity toEntity(Issuer domain) {
        IssuerEntity entity = new IssuerEntity();
        entity.setRuc(domain.getRuc().value());
        entity.setCorporateName(domain.getCorporateName());
        entity.setAddress(domain.getAddress());
        entity.setUbigeo(domain.getUbigeo());

        if (domain.getCertificate() != null) {
            entity.setCertBase64(domain.getCertificate().base64Content());
            entity.setCertPassword(domain.getCertificate().password());
            entity.setCertValidFrom(domain.getCertificate().validFrom());
            entity.setCertValidTo(domain.getCertificate().validTo());
        }

        Set<String> seriesCodes = domain.getAuthorizedSeries().stream()
                .map(AuthorizedSeries::code)
                .collect(Collectors.toSet());
        entity.setAuthorizedSeries(seriesCodes);

        return entity;
    }

    private Issuer toDomain(IssuerEntity entity) {
        DigitalCertificate cert = null;
        if (entity.getCertBase64() != null) {
            cert = new DigitalCertificate(
                    entity.getCertBase64(),
                    entity.getCertPassword(),
                    entity.getCertValidFrom(),
                    entity.getCertValidTo()
            );
        }

        Issuer domain = new Issuer(
                     new Ruc(entity.getRuc()),
                     entity.getCorporateName(),
                     entity.getAddress(),
                     entity.getUbigeo(),
                     cert
        );

        if (entity.getAuthorizedSeries() != null) {
            for (String seriesCode : entity.getAuthorizedSeries()) {
                domain.addAuthorizedSeries(new AuthorizedSeries(seriesCode));
            }
        }

        return domain;
    }
}
