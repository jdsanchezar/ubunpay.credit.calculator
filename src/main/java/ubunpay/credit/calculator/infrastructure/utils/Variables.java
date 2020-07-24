package ubunpay.credit.calculator.infrastructure.utils;

public enum Variables {
    VALUE_STUDY_CREDIT(57000),
    FUND_VALUE_GUATANREE(0.0833),
    SIX_MONTHS(6),
    TWELVE_MONTHS(12),
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