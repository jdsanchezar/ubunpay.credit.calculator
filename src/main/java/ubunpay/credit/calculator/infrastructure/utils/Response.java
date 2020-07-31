package ubunpay.credit.calculator.infrastructure.utils;

public enum Response {

    EXCEDE_CAPACIDAD_PAGO("Excede capacidad de pago");

    private String value;

    Response(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
