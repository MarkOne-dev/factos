package pe.factos.issuer.domain.port;

import pe.factos.issuer.domain.model.Issuer;
import pe.factos.issuer.domain.model.Ruc;

import java.util.Optional;

public interface IssuerRepository {
    void save(Issuer issuer);
    Optional<Issuer> findByRuc(Ruc ruc);
}
