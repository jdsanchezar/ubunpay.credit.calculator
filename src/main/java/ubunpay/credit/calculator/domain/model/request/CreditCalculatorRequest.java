package ubunpay.credit.calculator.domain.model.request;


import lombok.Data;
import ubunpay.credit.calculator.infrastructure.utils.Variables;

import java.util.ArrayList;

@Data
public class CreditCalculatorRequest {

    private boolean valueProduct;
    private double requestAmount;
    private double valueStudyCredit = Variables.VALUE_STUDY_CREDIT.getValue();
    private double discountFund = Variables.VALUE_DISCOUNT_FUND.getValue();
    private double maximumAmount = Variables.VALUE_MAXIMUM_AMOUNT.getValue();
    private double minimumAmount = Variables.VALUE_MINIMUM_AMOUNT.getValue();
    private ArrayList<Integer> minimumTerm;
}
