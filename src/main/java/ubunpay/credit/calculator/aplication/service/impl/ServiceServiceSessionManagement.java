package ubunpay.credit.calculator.aplication.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ubunpay.credit.calculator.aplication.IServiceSessionManagement;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;

import java.io.*;

@Service
public class ServiceServiceSessionManagement implements IServiceSessionManagement {

    public CreditCalculatorResponse generateToken() throws IOException, ParseException {
        File resource = new ClassPathResource("response.json").getFile();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(resource, CreditCalculatorResponse.class);
    }

}
