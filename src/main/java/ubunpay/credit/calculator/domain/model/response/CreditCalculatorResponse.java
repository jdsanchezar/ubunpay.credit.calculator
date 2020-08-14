package ubunpay.credit.calculator.domain.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ubunpay.commons.domain.model.CreditForMonths;

import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditCalculatorResponse {

    private ArrayList<CreditForMonths>  months;
    private double maxValue;

}
