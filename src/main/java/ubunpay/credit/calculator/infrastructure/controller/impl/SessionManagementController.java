package ubunpay.credit.calculator.infrastructure.controller.impl;

import io.swagger.annotations.ApiOperation;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ubunpay.credit.calculator.aplication.IServiceSessionManagement;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;
import ubunpay.credit.calculator.infrastructure.controller.ISessionManagementController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/ubuntec")
public class SessionManagementController implements ISessionManagementController {

    @Autowired
    IServiceSessionManagement handleSessionManagement;

    @ApiOperation(
            value = "getValueProduct")
    @RequestMapping(value = "/getValueProduct/{valueProduct}", method = RequestMethod.GET)
    public CreditCalculatorResponse getValueProduct(@PathVariable("valueProduct") double valueProduct) throws IOException, ParseException {
        return handleSessionManagement.generateToken();
    }

}
