package pe.factos.catalog.domain;

public enum TaxType {
    IGV("1000", "VAT", "IGV", "Impuesto General a las Ventas"),
    ISC("2000", "EXC", "ISC", "Impuesto Selectivo al Consumo"),
    EXONERATED("9997", "VAT", "EXO", "Exonerado"),
    INACTIVE("9998", "FRE", "INA", "Inafecto"),
    EXPORT("9995", "FRE", "EXP", "Exportación");

    private final String code;
    private final String internationalCode;
    private final String name;
    private final String description;

    TaxType(String code, String internationalCode, String name, String description) {
        this.code = code;
        this.internationalCode = internationalCode;
        this.name = name;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getInternationalCode() {
        return internationalCode;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static TaxType findByCode(String code) {
        for (TaxType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported tax type code: " + code);
    }
}
