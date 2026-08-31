package pe.factos.issuer.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.factos.issuer.domain.model.aggregates.Issuer;
import pe.factos.issuer.domain.model.valueobjects.Ruc;
import pe.factos.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;

@Entity
@Table(name = "issuers")
@Getter
@Setter
@NoArgsConstructor
public class IssuerJpaEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "ruc", nullable = false, unique = true, length = 11)
    private String ruc;

    @Column(name = "corporate_name", nullable = false)
    private String corporateName;

    @Column(name = "address")
    private String address;

    @Column(name = "ubigeo", length = 6)
    private String ubigeo;

    public IssuerJpaEntity(Issuer issuer) {
        this.ruc = issuer.getRuc().value();
        this.corporateName = issuer.getCorporateName();
        this.address = issuer.getAddress();
        this.ubigeo = issuer.getUbigeo();
    }

    public Issuer toDomain() {
        return new Issuer(new Ruc(ruc), corporateName, address, ubigeo);
    }
}
