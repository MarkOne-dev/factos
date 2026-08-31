package pe.factos.issuer.domain.repositories;

import pe.factos.issuer.domain.model.aggregates.Issuer;
import pe.factos.issuer.domain.model.valueobjects.Ruc;

import java.util.Optional;

public interface IssuerRepository {
    Issuer save(Issuer issuer);
    Optional<Issuer> findByRuc(Ruc ruc);
    boolean existsByRuc(Ruc ruc);
}
