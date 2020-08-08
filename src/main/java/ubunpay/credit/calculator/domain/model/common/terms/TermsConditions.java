package ubunpay.credit.calculator.domain.model.common.terms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermsConditions {

    private String name;
    private boolean checked;
    private Date date;
}
