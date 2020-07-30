package ubunpay.credit.calculator.domain.model.response;

import lombok.Data;

@Data
public class CreditForMonths {


    private double valueToFinance;
    private double studyCredit;
    private double otherCosts;
    private int dueTime;
    private double monthlyFee;
    private double bankGuarantee;
    private double lifeInsurance;
    private double annualEffectiveRate;

}
