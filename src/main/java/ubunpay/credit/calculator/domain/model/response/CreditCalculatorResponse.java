package ubunpay.credit.calculator.domain.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCalculatorResponse {

    private ArrayList<CreditForMonths>  credits06;
    private ArrayList<CreditForMonths>  credits12;
    private ArrayList<CreditForMonths>  credits18;
    private ArrayList<CreditForMonths>  credits24;
    private ArrayList<CreditForMonths>  credits30;
    private ArrayList<CreditForMonths>  credits36;

}
