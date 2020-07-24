package ubunpay.credit.calculator.domain.model.request;


import lombok.Data;

import java.util.ArrayList;

@Data
public class CreditCalculatorRequest {

    private boolean valueProduct;
    private double requestAmount;
    private boolean valueStudyCredit;
    private int discountFund;
    private ArrayList<Integer> minimumTerm;
}
