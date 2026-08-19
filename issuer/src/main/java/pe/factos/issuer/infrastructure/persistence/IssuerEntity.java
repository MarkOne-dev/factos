package pe.factos.issuer.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "issuers")
@Getter
@Setter
public class IssuerEntity {
    @Id
    @Column(name = "ruc", length = 11)
    private String ruc;

    @Column(name = "corporate_name", nullable = false)
    private String corporateName;

    @Column(name = "address")
    private String address;

    @Column(name = "ubigeo", length = 6)
    private String ubigeo;

    @Column(name = "cert_base64")
    private String certBase64;

    @Column(name = "cert_password")
    private String certPassword;

    @Column(name = "cert_valid_from")
    private Instant certValidFrom;

    @Column(name = "cert_valid_to")
    private Instant certValidTo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "authorized_series",
        joinColumns = @JoinColumn(name = "issuer_ruc")
    )
    @Column(name = "code", length = 4)
    private Set<String> authorizedSeries = new HashSet<>();
}
