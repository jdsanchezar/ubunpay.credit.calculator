package ubunpay.credit.calculator.infrastructure.utils;

public enum Variables {
    VALUE_STUDY_CREDIT(57000),
    VALUE_DISCOUNT_FUND(0.0833),
    VALUE_MINIMUM_AMOUNT(500000),
    VALUE_MAXIMUM_AMOUNT(10000000),
    ONE(1),
    VALUE_INCOGNITO(5180),
    TWELVE_MONTHS(12),
    THIRTY_SIX_MONTHS(36);

    private double value;

    Variables(double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }
}