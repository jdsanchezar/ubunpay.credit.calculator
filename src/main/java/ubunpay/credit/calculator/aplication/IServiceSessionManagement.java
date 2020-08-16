package ubunpay.credit.calculator.aplication;


import org.json.simple.parser.ParseException;

import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IServiceSessionManagement {

    public CreditCalculatorResponse generateToken() throws IOException, ParseException;

    public CreditCalculatorResponse calculateCreditProduct(String token) throws URISyntaxException, IOException;

    public void saveInfoCredit(CreditCalculatorResponse creditCalculatorResponse,String token) throws URISyntaxException, IOException;

    public CreditCalculatorResponse getCalculateValuesForCredit(String token, double value ) throws URISyntaxException, IOException;
}
