package ubunpay.credit.calculator.infrastructure.utils;

public enum TipoCredito {

    VALUE_COMPRA_PRODUCTOS("compra productos"),
    VALUE_LIBRE_INVERSION("libre inversion");

    private String value;

    TipoCredito(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
