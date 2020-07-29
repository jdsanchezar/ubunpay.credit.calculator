package ubunpay.credit.calculator.domain.model.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {

    private String idAssociated;
    private Boolean termsAndConditions;
    private int idbeneficiary;
    private int idProduct;
    private String nameProduct;
    private String agreement;
    private Double price;
}


