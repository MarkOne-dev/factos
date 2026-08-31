package pe.factos.catalog.domain;

public enum IgvAffectationType {
    TAXABLE_ONEROUS("10", "Gravado - Operación Onerosa", true, false),
    TAXABLE_RETIREMENT_PREMIUM("11", "Gravado - Retiro por premio", true, true),
    TAXABLE_RETIREMENT_ADVERTISING("12", "Gravado - Retiro por publicidad", true, true),
    TAXABLE_RETIREMENT_BONUS("13", "Gravado - Retiro por bonificación", true, true),
    TAXABLE_RETIREMENT_EMPLOYEES("14", "Gravado - Retiro por entrega a trabajadores", true, true),
    EXONERATED_ONEROUS("20", "Exonerado - Operación Onerosa", false, false),
    INACTIVE_ONEROUS("30", "Inafecto - Operación Onerosa", false, false),
    INACTIVE_RETIREMENT("31", "Inafecto - Retiro", false, true),
    EXPORT_SERVICES("40", "Exportación de Servicios", false, false);

    private final String code;
    private final String description;
    private final boolean taxable;
    private final boolean free;

    IgvAffectationType(String code, String description, boolean taxable, boolean free) {
        this.code = code;
        this.description = description;
        this.taxable = taxable;
        this.free = free;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public boolean isFree() {
        return free;
    }

    public static IgvAffectationType findByCode(String code) {
        for (IgvAffectationType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported IGV affectation type code: " + code);
    }
}
