package ubunpay.credit.calculator.domain.model.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserModel {

	private String orderid;
    private Double totalValue;
    private Double totalValueDiscount;
    private String customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerEMail;
    private String customerPhoneNumber;
    private String custmerIp;
    private String OrderReturnURL;
    private String cedulap;
    private String correop;
    private String phonep;
    private String channelCode;
    
    private String nameProduct;
    private String agreement;
}


