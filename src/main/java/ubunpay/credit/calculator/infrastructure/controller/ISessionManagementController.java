package ubunpay.credit.calculator.infrastructure.controller;

import org.json.simple.parser.ParseException;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;

import java.io.IOException;

public interface ISessionManagementController {


    public CreditCalculatorResponse getValueProduct(double valueProduct) throws IOException, ParseException;

}
