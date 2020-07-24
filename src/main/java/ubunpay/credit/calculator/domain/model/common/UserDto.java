package ubunpay.credit.calculator.domain.model.common;

import lombok.Data;

@Data
public class UserDto {

    private int identificationCard;
    private double maximumAmount;
}
