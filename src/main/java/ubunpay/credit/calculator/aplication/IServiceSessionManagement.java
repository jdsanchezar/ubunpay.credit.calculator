package ubunpay.credit.calculator.aplication;


import org.json.simple.parser.ParseException;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;

import java.io.IOException;

public interface IServiceSessionManagement {

    public CreditCalculatorResponse generateToken() throws IOException, ParseException;
}
