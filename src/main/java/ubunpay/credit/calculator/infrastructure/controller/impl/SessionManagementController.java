package ubunpay.credit.calculator.infrastructure.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ubunpay.credit.calculator.aplication.IServiceSessionManagement;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;
import ubunpay.credit.calculator.domain.model.response.CreditForMonths;
import ubunpay.credit.calculator.infrastructure.controller.ISessionManagementController;
import ubunpay.credit.calculator.infrastructure.persistence.entidad.PreAprobadosEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/ubuntec")
public class SessionManagementController implements ISessionManagementController {

    @Autowired
    IServiceSessionManagement handleSessionManagement;

    @ApiOperation(
            value = "getValueProduct")
    @RequestMapping(value = "/getValueProduct/{valueProduct}", method = RequestMethod.GET)
    public CreditCalculatorResponse getValueProduct(@PathVariable("valueProduct") double valueProduct) throws IOException, ParseException {
        CreditCalculatorResponse handleSession = new CreditCalculatorResponse();
        try {
            handleSession= handleSessionManagement.generateToken();
        } catch (Exception e) {
            e.printStackTrace();
            CreditForMonths creditForMonths = new CreditForMonths();
            creditForMonths.setError(e.getMessage());
            ArrayList<CreditForMonths> array = new ArrayList<>();
            array.add(creditForMonths);
            handleSession.setMonths(array);
        }
        return handleSession;
    }

    @GetMapping("/message")
    public String getMessage() {
        return "Welcome222 to JavaTechie..!!";
    }

    @ApiOperation(
            value = "getCalculate")
    @RequestMapping(value = "/getCalculate/{token}", method = RequestMethod.GET)
    public PreAprobadosEntity getCalculate(@PathVariable("token") String token) {
        PreAprobadosEntity preAprobadosEntity = new PreAprobadosEntity();
        try {
         preAprobadosEntity =  handleSessionManagement.getProductById(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preAprobadosEntity;
    }


}
