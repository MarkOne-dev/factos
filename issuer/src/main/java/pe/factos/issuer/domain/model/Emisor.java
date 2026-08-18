package pe.factos.issuer.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Emisor {
    private String ruc;
    private String razonSocial;
    private String ubigeo;
    private String direccion;
    private String provincia;
    private String departamento;
    private String distrito;
}
