package ubunpay.credit.calculator.domain.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ubunpay.credit.calculator.domain.model.response.CreditForMonthsOld;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditInfoOld {

    private String selectedTerm;
    ArrayList<CreditForMonthsOld> months;
}
