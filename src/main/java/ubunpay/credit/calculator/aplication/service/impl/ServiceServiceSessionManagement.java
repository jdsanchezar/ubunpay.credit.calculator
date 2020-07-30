package ubunpay.credit.calculator.aplication.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ubunpay.credit.calculator.aplication.IServiceSessionManagement;
import ubunpay.credit.calculator.domain.model.common.UserModel;
import ubunpay.credit.calculator.domain.model.request.CreditCalculatorRequest;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;
import ubunpay.credit.calculator.infrastructure.persistence.entidad.PreAprobadosEntity;
import ubunpay.credit.calculator.infrastructure.persistence.repositorio.jpa.RepositorioPreAprobadosJPA;
import ubunpay.credit.calculator.infrastructure.utils.Variables;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class ServiceServiceSessionManagement implements IServiceSessionManagement {

    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    private RepositorioPreAprobadosJPA repository;

    public CreditCalculatorResponse generateToken() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:response.json");
        InputStream dbAsStream = resource.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(dbAsStream, CreditCalculatorResponse.class);
    }


    @Override
    public CreditCalculatorResponse getProductById(String token) throws URISyntaxException, IOException {
        CreditCalculatorResponse calculatorResponse = new CreditCalculatorResponse();
        Resource resource = resourceLoader.getResource("classpath:response.json");
        InputStream dbAsStream = resource.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        calculatorResponse = objectMapper.readValue(dbAsStream, CreditCalculatorResponse.class);


        PreAprobadosEntity preAprobadosEntity = new PreAprobadosEntity();
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = System.getenv().get("getDtoWithToken") + "/" + token;
        URI uri = new URI(baseUrl);
        UserModel userModel = objectMapper.readValue(restTemplate.getForObject(uri, String.class), UserModel.class);
        preAprobadosEntity = repository.findById(Integer.valueOf(userModel.getIdAssociated())).orElse(null);
        tranformacion(calculatorResponse, preAprobadosEntity, userModel,
                calculadora(userModel.getPrice(), preAprobadosEntity));
        return calculatorResponse;
    }


    private double calculadora(Double valorProducto, PreAprobadosEntity preAprobadosEntity) {
        CreditCalculatorRequest credit = new CreditCalculatorRequest();
        double valor = valorProducto / (Variables.ONE.getValue() - credit.getDiscountFund()) +
                credit.getValueStudyCredit() + Variables.VALUE_INCOGNITO.getValue();
        if (valor > preAprobadosEntity.getValidacion()) {
            System.out.println("Supera monto pre aprobado");
        }
        return valor;
    }

    private CreditCalculatorResponse tranformacion(CreditCalculatorResponse cal, PreAprobadosEntity preAprobadosEntity,
                                                   UserModel userModel, double valor) {
        double pago;
        double interes = preAprobadosEntity.getTasa().doubleValue()+0.09;
        double valor1 = interes * ((1 + interes));
        double valor2 = ((1 + interes));
        //   pago = valor * ((interes * ((1 + interes) **12) / (((1 + interes)**12) - 1)));
        // pago = (valor/Variables.TWELVE_MONTHS.getValue())*(preAprobadosEntity.getTasa().doubleValue()+0.09);
       pago = valor * Math.pow(valor1,12) /(Math.pow(valor2,12) -1);
        CreditCalculatorRequest credit = new CreditCalculatorRequest();
        cal.getMonths().forEach(e -> {
            e.setValueToFinance(userModel.getPrice());
            e.setStudyCredit(credit.getValueStudyCredit());
        });
        return cal;
    }

}
