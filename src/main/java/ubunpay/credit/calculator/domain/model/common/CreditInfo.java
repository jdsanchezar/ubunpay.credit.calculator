package ubunpay.credit.calculator.domain.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ubunpay.credit.calculator.domain.model.response.CreditForMonths;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditInfo {

    private String selectedTerm;
    ArrayList<CreditForMonths> months;
}
