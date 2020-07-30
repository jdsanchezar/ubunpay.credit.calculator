package ubunpay.credit.calculator.aplication;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.parser.ParseException;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;
import ubunpay.credit.calculator.infrastructure.persistence.entidad.PreAprobadosEntity;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IServiceSessionManagement {

    public CreditCalculatorResponse generateToken() throws IOException, ParseException;

    public CreditCalculatorResponse getProductById(String token) throws URISyntaxException, IOException;
}
