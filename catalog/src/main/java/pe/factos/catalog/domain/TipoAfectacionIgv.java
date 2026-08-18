package pe.factos.catalog.domain;

public enum TipoAfectacionIgv {
    GRAVADO_OPERACION_ONEROSA("10", "Gravado - Operación Onerosa", true, false),
    GRAVADO_RETIRO("11", "Gravado - Retiro por premio", true, true),
    GRAVADO_RETIRO_PUBLICIDAD("12", "Gravado - Retiro por publicidad", true, true),
    GRAVADO_RETIRO_BONIFICACION("13", "Gravado - Retiro por bonificación", true, true),
    GRAVADO_RETIRO_ENTREGA_TRABAJADORES("14", "Gravado - Retiro por entrega a trabajadores", true, true),
    EXONERADO_OPERACION_ONEROSA("20", "Exonerado - Operación Onerosa", false, false),
    INAFECTO_OPERACION_ONEROSA("30", "Inafecto - Operación Onerosa", false, false),
    INAFECTO_RETIRO("31", "Inafecto - Retiro", false, true),
    EXPORTACION_SERVICIOS("40", "Exportación de Servicios", false, false);

    private final String codigo;
    private final String descripcion;
    private final boolean gravado;
    private final boolean gratuito;

    TipoAfectacionIgv(String codigo, String descripcion, boolean gravado, boolean gratuito) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.gravado = gravado;
        this.gratuito = gratuito;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isGravado() {
        return gravado;
    }

    public boolean isGratuito() {
        return gratuito;
    }

    public static TipoAfectacionIgv buscarPorCodigo(String codigo) {
        for (TipoAfectacionIgv tipo : values()) {
            if (tipo.codigo.equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código de tipo de afectación de IGV no soportado: " + codigo);
    }
}
