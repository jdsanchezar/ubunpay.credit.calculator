package ubunpay.credit.calculator.infrastructure.controller;

import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.PathVariable;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;
import ubunpay.credit.calculator.infrastructure.persistence.entidad.PreAprobadosEntity;

import java.io.IOException;

public interface ISessionManagementController {


    public CreditCalculatorResponse getValueProduct(double valueProduct) throws IOException, ParseException;

    public CreditCalculatorResponse getCalculate(@PathVariable("token") String token);
}
