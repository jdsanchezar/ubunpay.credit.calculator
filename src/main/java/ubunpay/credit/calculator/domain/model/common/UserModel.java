package ubunpay.credit.calculator.domain.model.common;


import lombok.Data;
import ubunpay.credit.calculator.domain.model.common.terms.TermsConditions;

import org.springframework.data.annotation.Id;

import java.lang.annotation.Documented;
import java.util.ArrayList;

@Data
public class UserModel {

	@Id
    private String sessionId;
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
    private ArrayList<UserDocument> userDocuments;
    private CreditInfo creditInfo;
    private ArrayList<TermsConditions> termsConditions;
}


