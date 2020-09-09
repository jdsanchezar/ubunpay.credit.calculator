package ubunpay.credit.calculator.infrastructure.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ubunpay.credit.calculator.aplication.IServiceSessionManagement;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;
import ubunpay.credit.calculator.domain.model.response.ErrorResponse;
import ubunpay.credit.calculator.infrastructure.controller.ISessionManagementController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/ubuntec")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class SessionManagementController implements ISessionManagementController {

    @Autowired
    IServiceSessionManagement handleSessionManagement;

    @ApiOperation(
            value = "getValueProduct")
    @RequestMapping(value = "/getValueProduct/", method = RequestMethod.GET)
    public CreditCalculatorResponse getValueProduct(@RequestHeader("valueProduct") double valueProduct) throws IOException, ParseException {
        CreditCalculatorResponse handleSession = new CreditCalculatorResponse();
        try {
            handleSession = handleSessionManagement.generateToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handleSession;
    }

    @GetMapping("/message")
    public String getMessage() {
        String name = "Heisohn";
        System.out.println(name.substring(3, 5));
        return "V20200824_01";
    }

    @ApiOperation(
            value = "getCalculate")
    @RequestMapping(value = "/getCalculate/", method = RequestMethod.GET)
    public ResponseEntity getCalculate(@RequestParam("token") String token) {
        CreditCalculatorResponse creditCalculatorResponse = new CreditCalculatorResponse();
        try {
        	//Hacer llamado unico al get token
            creditCalculatorResponse = handleSessionManagement.calculateCreditProduct(token);
            handleSessionManagement.saveInfoCredit(creditCalculatorResponse,token);
            return new ResponseEntity(creditCalculatorResponse, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorMessage(e.getMessage());
            return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(
            value = "getCalculateFromValue")
    @RequestMapping(value = "/getCalculateFromValue/", method = RequestMethod.GET)
    public ResponseEntity getCalculateFromValue(@RequestParam("token")String token,@RequestParam("value") Double value) {
        ErrorResponse errorResponse = new ErrorResponse();
        try {            
            CreditCalculatorResponse creditCalculatorResponse = handleSessionManagement.getCalculateValuesForCredit(token, value);
            handleSessionManagement.saveInfoCredit(creditCalculatorResponse,token);
            return new ResponseEntity(creditCalculatorResponse, HttpStatus.ACCEPTED);
            
        } catch (Exception e) {
            errorResponse.setErrorMessage(e.getMessage());
            errorResponse.setError(true);
            return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
