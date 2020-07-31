package ubunpay.credit.calculator.domain.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
	private boolean error;
	private String errorMessage;
}
