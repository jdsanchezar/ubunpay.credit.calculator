package ubunpay.credit.calculator.aplication.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ubunpay.credit.calculator.aplication.IServiceSessionManagement;
import ubunpay.credit.calculator.domain.model.common.CreditInfo;
import ubunpay.credit.calculator.domain.model.common.UserModel;
import ubunpay.credit.calculator.domain.model.request.CreditCalculatorRequest;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;
import ubunpay.credit.calculator.domain.model.response.ErrorResponse;
import ubunpay.credit.calculator.infrastructure.persistence.entidad.PreAprobadosEntity;
import ubunpay.credit.calculator.infrastructure.persistence.repositorio.jpa.RepositorioPreAprobadosJPA;
import ubunpay.credit.calculator.infrastructure.utils.Response;
import ubunpay.credit.calculator.infrastructure.utils.Variables;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        ErrorResponse error = new ErrorResponse();
        PreAprobadosEntity preAprobadosEntity = new PreAprobadosEntity();
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = System.getenv().get("getDtoWithToken") + "/?token=" + token;
        URI uri = new URI(baseUrl);
        UserModel userModel = objectMapper.readValue(restTemplate.getForObject(uri, String.class), UserModel.class);
        //  userModel.
        double valor = 0;
        if (userModel.getCedulap() != null) {
            preAprobadosEntity = repository.findById(Integer.valueOf(userModel.getCedulap())).orElse(null);
            if (userModel.getTotalValueDiscount() != null) {
                if (userModel.getTotalValueDiscount() > 0) {
                    valor = calculadora(userModel.getTotalValueDiscount(), preAprobadosEntity);
                    Double valueWithDiscount = preAprobadosEntity.getValidacion() - preAprobadosEntity.getDescuentoCredito();
                    System.out.println(valueWithDiscount);
                } else {
                    System.out.println("Aqui se calculara el valor del prestamo predefinido si viene en 0");
                    System.out.println("<<<<<<<< ------- Valor 0 desde MARKETPLACE: ---------- >>>>>>>>");
                    System.out.println("<<<<<<<< ------- Valor obtenido desde MYSQL: " + preAprobadosEntity.getValidacion() + "---------- >>>>>>>>");
                    valor = preAprobadosEntity.getValidacion();
                    valor =restarValorDesdeMysql(valor, preAprobadosEntity);
                    calculatorResponse.setMaxValue(preAprobadosEntity.getValidacion());
                }
            } else {
                System.out.println("<<<<<<<< ------- Valor Null desde MARKETPLACE: ---------- >>>>>>>>");
                System.out.println("<<<<<<<< ------- Valor obtenido desde MYSQL: " + preAprobadosEntity.getValidacion() + "---------- >>>>>>>>");
                valor = preAprobadosEntity.getValidacion();
                valor =restarValorDesdeMysql(valor, preAprobadosEntity);
                calculatorResponse.setMaxValue(preAprobadosEntity.getValidacion());
            }
            if (valor >= preAprobadosEntity.getValidacion()) {
                error.setError(true);
                error.setErrorMessage(Response.EXCEDE_CAPACIDAD_PAGO.getValue());
            } else {
                if (valor != 0) {
                    tranformacion(calculatorResponse, preAprobadosEntity, userModel, valor, error);
                }
            }
        }
        calculatorResponse.setErrorResponse(error);
        if (calculatorResponse.getErrorResponse().isError()) {
            calculatorResponse.setMonths(new ArrayList<>());
        }
        return calculatorResponse;
    }

    @Async
    public void saveInfoCredit(CreditCalculatorResponse creditCalculatorResponse, String token) throws
            URISyntaxException, IOException {
        System.out.println("Inicia doLogAsync: " + System.currentTimeMillis());
        try {
            TimeUnit.SECONDS.sleep(2);
            RestTemplate restTemplate = new RestTemplate();
            final String baseUrl = System.getenv().get("get-session-management-addDataToSession");
            URI uri = new URI(baseUrl);
            ObjectMapper objectMapper = new ObjectMapper();
            restTemplate.postForEntity(uri, loadUserModelWithCalculate(returnUserModel(token), creditCalculatorResponse),
                    String.class);
            System.out.println(baseUrl);
        } catch (InterruptedException e) {
            System.out.println("Error sleep: " + System.currentTimeMillis());
            e.printStackTrace();
        }
        ;
        System.out.println("Fin doLogAsync: " + System.currentTimeMillis());
    }


    private UserModel returnUserModel(String token) throws URISyntaxException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        final String baseUrl = System.getenv().get("getDtoWithToken") + "/?token=" + token;
        URI uri = new URI(baseUrl);
        return objectMapper.readValue(restTemplate.getForObject(uri, String.class), UserModel.class);

    }

    private UserModel loadUserModelWithCalculate(UserModel userModel,
                                                 CreditCalculatorResponse creditCalculatorResponse) {
        if (userModel.getCreditInfo() != null) {
            CreditInfo cre = userModel.getCreditInfo();
            cre.setMonths(creditCalculatorResponse.getMonths());
            userModel.setCreditInfo(cre);
            userModel.getCreditInfo().setMaxValue(creditCalculatorResponse.getMaxValue());
        } else {
            CreditInfo cre = new CreditInfo();
            cre.setMonths(creditCalculatorResponse.getMonths());
            userModel.setCreditInfo(cre);
        }

        return userModel;
    }

    private double calculadora(Double valorProducto, PreAprobadosEntity preAprobadosEntity) {
        CreditCalculatorRequest credit = new CreditCalculatorRequest();
        double valor = valorProducto / (Variables.ONE.getValue() - credit.getDiscountFund()) +
                credit.getValueStudyCredit() + Variables.VALUE_INCOGNITO.getValue();
        return valor;
    }

    private double restarValorDesdeMysql(Double valorProducto, PreAprobadosEntity preAprobadosEntity) {
        System.out.println("<<<<<<<< ------- Calculando el valor menos los sobrecargos predefinidos: ---------- >>>>>>>>");
        CreditCalculatorRequest credit = new CreditCalculatorRequest();
        double restar = valorProducto / (Variables.ONE.getValue() - credit.getDiscountFund()) +
                credit.getValueStudyCredit() + Variables.VALUE_INCOGNITO.getValue();
        double valor = restar-valorProducto;
        System.out.println("<<<<<<<< ------- Sobrecargos :  " + valor+ "---------- >>>>>>>>");
        valor= valorProducto -valor;
        System.out.println("<<<<<<<< ------- Valor sin sobrecargo :  " + valor+ "---------- >>>>>>>>");
        return valor;
    }

    private CreditCalculatorResponse tranformacion(CreditCalculatorResponse cal, PreAprobadosEntity preAprobadosEntity,
                                                   UserModel userModel, double valor, ErrorResponse error) {

        double interes = (preAprobadosEntity.getTasa().doubleValue());
        double valor1 = ((1 + interes));
        double valor2 = ((1 + interes));
        double pago6 = (valor * (((Math.pow(valor1, 6)) * interes) / ((Math.pow(valor2, 6)) - 1))) +
                (valor * (preAprobadosEntity.getSeguroVidaPor().doubleValue() / 100));
        double pago12 = (valor * (((Math.pow(valor1, 12)) * interes) / ((Math.pow(valor2, 12)) - 1))) +
                (valor * (preAprobadosEntity.getSeguroVidaPor().doubleValue() / 100));
        ;
        double pago18 = (valor * (((Math.pow(valor1, 18)) * interes) / ((Math.pow(valor2, 18)) - 1))) +
                (valor * (preAprobadosEntity.getSeguroVidaPor().doubleValue() / 100));
        ;
        double pago24 = (valor * (((Math.pow(valor1, 24)) * interes) / ((Math.pow(valor2, 24)) - 1))) +
                (valor * (preAprobadosEntity.getSeguroVidaPor().doubleValue() / 100));
        ;
        double pago30 = (valor * (((Math.pow(valor1, 30)) * interes) / ((Math.pow(valor2, 30)) - 1))) +
                (valor * (preAprobadosEntity.getSeguroVidaPor().doubleValue() / 100));
        ;
        double pago36 = (valor * (((Math.pow(valor1, 36)) * interes) / ((Math.pow(valor2, 36)) - 1))) +
                (valor * (preAprobadosEntity.getSeguroVidaPor().doubleValue() / 100));
        ;
        CreditCalculatorRequest credit = new CreditCalculatorRequest();
        cal.getMonths().forEach(e -> {
            e.setValueToFinance(valor);
            e.setStudyCredit(credit.getValueStudyCredit());
            e.setAnnualEffectiveRate(((Math.pow((1 + preAprobadosEntity.getTasa().doubleValue()), 12)) - 1) * 100);
            e.setBankGuarantee(valor * Variables.VALUE_DISCOUNT_FUND.getValue());
            e.setLifeInsurance(preAprobadosEntity.getSeguroVida());
            e.setOtherCosts(preAprobadosEntity.getTasa().doubleValue() * valor + preAprobadosEntity.getSeguroVida());
            e.setNominalMonthPastDue(preAprobadosEntity.getTasa().doubleValue() * 100);
            switch (e.getDueTime()) {
                case 6:
                    e.setMonthlyFee(pago6);
                    break;
                case 12:
                    e.setMonthlyFee(pago12);
                    break;
                case 18:
                    e.setMonthlyFee(pago18);
                    break;
                case 24:
                    e.setMonthlyFee(pago24);
                    break;
                case 30:
                    e.setMonthlyFee(pago30);
                    break;
                case 36:
                    e.setMonthlyFee(pago36);
                    break;
                default:
            }
        });
        cal.setMonths((ArrayList) cal.getMonths().stream().filter(month -> month.getMonthlyFee() <= preAprobadosEntity.getCapacidadPago()).collect(Collectors.toList()));
        error.setError(false);
        return cal;
    }


}
