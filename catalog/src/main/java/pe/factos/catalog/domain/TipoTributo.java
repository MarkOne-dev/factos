package pe.factos.catalog.domain;

public enum TipoTributo {
    IGV("1000", "VAT", "IGV", "Impuesto General a las Ventas"),
    ISC("2000", "EXC", "ISC", "Impuesto Selectivo al Consumo"),
    EXONERADO("9997", "VAT", "EXO", "Exonerado"),
    INAFECTO("9998", "FRE", "INA", "Inafecto"),
    EXPORTACION("9995", "FRE", "EXP", "Exportación");

    private final String codigo;
    private final String codigoInternacional;
    private final String nombre;
    private final String descripcion;

    TipoTributo(String codigo, String codigoInternacional, String nombre, String descripcion) {
        this.codigo = codigo;
        this.codigoInternacional = codigoInternacional;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCodigoInternacional() {
        return codigoInternacional;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoTributo buscarPorCodigo(String codigo) {
        for (TipoTributo tributo : values()) {
            if (tributo.codigo.equals(codigo)) {
                return tributo;
            }
        }
        throw new IllegalArgumentException("Código de tributo no soportado: " + codigo);
    }
}
