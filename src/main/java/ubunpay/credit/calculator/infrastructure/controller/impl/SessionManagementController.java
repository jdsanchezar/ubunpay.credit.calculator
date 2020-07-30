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
        }
        return handleSession;
    }

    @GetMapping("/message")
    public String getMessage() {
        String name = "Heisohn";
        System.out.println(name.substring(3,5));
        return "Welcome to JavaTechie..!!";
    }

    @ApiOperation(
            value = "getCalculate")
    @RequestMapping(value = "/getCalculate/{token}", method = RequestMethod.GET)
    public CreditCalculatorResponse getCalculate(@PathVariable("token") String token) {
        CreditCalculatorResponse preAprobadosEntity = new CreditCalculatorResponse();
        try {
         preAprobadosEntity =  handleSessionManagement.getProductById(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preAprobadosEntity;
    }


}
