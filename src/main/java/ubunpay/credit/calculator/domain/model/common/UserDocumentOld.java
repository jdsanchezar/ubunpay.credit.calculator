package ubunpay.credit.calculator.domain.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDocumentOld {

    private String partner;
    private String documentName;
    private boolean isPresent;
}
