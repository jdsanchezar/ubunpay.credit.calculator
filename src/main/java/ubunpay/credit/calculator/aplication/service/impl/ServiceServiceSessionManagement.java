package ubunpay.credit.calculator.aplication.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import ubunpay.credit.calculator.aplication.IServiceSessionManagement;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;

import java.io.*;

@Service
public class ServiceServiceSessionManagement implements IServiceSessionManagement {

    @Autowired
    ResourceLoader resourceLoader;

    public CreditCalculatorResponse generateToken() throws IOException, ParseException {
        Resource resource = resourceLoader.getResource("classpath:response.json");
        InputStream dbAsStream = resource.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(dbAsStream, CreditCalculatorResponse.class);
    }

}
