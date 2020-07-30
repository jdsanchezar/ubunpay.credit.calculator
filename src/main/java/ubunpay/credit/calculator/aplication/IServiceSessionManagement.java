package ubunpay.credit.calculator.aplication;


import org.json.simple.parser.ParseException;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IServiceSessionManagement {

    public CreditCalculatorResponse generateToken() throws IOException, ParseException;

    public CreditCalculatorResponse getProductById(String token) throws URISyntaxException, IOException;
}
