package ubunpay.credit.calculator.infrastructure.utils;

public enum Variables {
    VALUE_STUDY_CREDIT(57000),
    VALUE_DISCOUNT_FUND(0.0833),
    VALUE_MAXIMUM_AMOUNT(500000),
    VALUE_MINIMUM_AMOUNT(10000000),
    EIGHTEEN_MONTHS(18),
    TWENTY_FOUR_MONTHS(24),
    THIRTY_MONTHS(30),
    THIRTY_SIX_MONTHS(36);

    private double value;

    Variables(double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }
}