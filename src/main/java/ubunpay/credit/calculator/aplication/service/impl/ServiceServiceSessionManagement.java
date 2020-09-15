package ubunpay.credit.calculator.aplication.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ubunpay.commons.domain.constant.UbunpayConstans;
import ubunpay.commons.domain.model.CreditForMonths;
import ubunpay.commons.domain.model.CreditInfo;
import ubunpay.commons.domain.model.Customer;
import ubunpay.commons.domain.model.SessionModel;
import ubunpay.commons.domain.model.calc.PreAprobadosEntity;
import ubunpay.commons.zoho.application.service.IZohoIntegrationService;
import ubunpay.commons.zoho.domain.model.ResultRecordZoho;
import ubunpay.commons.zoho.integration.infrastructure.utils.ZohoPhasesEnum;
import ubunpay.credit.calculator.aplication.IServiceSessionManagement;
import ubunpay.credit.calculator.domain.model.request.CreditCalculatorRequest;
import ubunpay.credit.calculator.domain.model.response.CreditCalculatorResponse;
import ubunpay.credit.calculator.domain.model.response.ErrorResponse;
import ubunpay.credit.calculator.infrastructure.persistence.repositorio.jpa.RepositorioPreAprobadosJPA;
import ubunpay.credit.calculator.infrastructure.utils.Response;
import ubunpay.credit.calculator.infrastructure.utils.TipoCredito;
import ubunpay.credit.calculator.infrastructure.utils.Variables;

@Service
public class ServiceServiceSessionManagement implements IServiceSessionManagement {

	@Autowired
	ResourceLoader resourceLoader;
	@Autowired
	private RepositorioPreAprobadosJPA repository;
	@Autowired
	IZohoIntegrationService zohoIntegrationService;

	public CreditCalculatorResponse generateToken() throws IOException {
		Resource resource = resourceLoader.getResource("classpath:response.json");
		InputStream dbAsStream = resource.getInputStream();
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(dbAsStream, CreditCalculatorResponse.class);
	}

	private ArrayList<CreditForMonths> calculateCreditFee(double valorSolicitado, double tasaInteres,
			double porcentajeSeguroVida, double paymentCapacity, double amountToBePaid, String bankAccount) {
		CreditForMonths creditForMonths;
		ArrayList<CreditForMonths> arrayCreditForMonths = new ArrayList<>();
		double valor1 = ((1 + tasaInteres));
		double pago;
		for (int i = 6; i <= 36; i += 6) {
			creditForMonths = new CreditForMonths();
			pago = (valorSolicitado * (((Math.pow(valor1, i)) * tasaInteres) / ((Math.pow(valor1, i)) - 1)))
					+ (valorSolicitado * (porcentajeSeguroVida));
			creditForMonths.setMonthlyFee(pago);
			creditForMonths.setDueTime(i);
			creditForMonths.setValueToFinance(valorSolicitado);
			creditForMonths.setStudyCredit(Variables.VALUE_STUDY_CREDIT.getValue());
			creditForMonths.setAnnualEffectiveRate(((Math.pow((1 + tasaInteres), 12)) - 1) * 100);
			creditForMonths.setBankGuarantee(valorSolicitado * Variables.VALUE_DISCOUNT_FUND.getValue());
			creditForMonths.setLifeInsurance(porcentajeSeguroVida);
			creditForMonths.setOtherCosts(tasaInteres * valorSolicitado + porcentajeSeguroVida);
			creditForMonths.setNominalMonthPastDue(tasaInteres * 100);
			creditForMonths.setAmountToBePaid(amountToBePaid);
			creditForMonths.setBankAccount(bankAccount);
			if (pago <= paymentCapacity) {
				arrayCreditForMonths.add(creditForMonths);
			}
		}
		return arrayCreditForMonths;

	}

	public CreditCalculatorResponse calculateCreditProduct(SessionModel userModel)
			throws URISyntaxException, JsonProcessingException {

		CreditCalculatorResponse calculatorResponse = new CreditCalculatorResponse();
		ErrorResponse error = new ErrorResponse();
		PreAprobadosEntity preAprobadosEntity = new PreAprobadosEntity();

		if (userModel.getCedulap() != null) {

			preAprobadosEntity = repository.findById(Integer.valueOf(userModel.getCedulap())).orElse(null);
			double creditLimit;
			double creditValue;
			double requestedAmount;

			calculatorResponse.setBankAccount(preAprobadosEntity.getBankAccount());

			if (userModel.getTotalValueDiscount() == null || userModel.getTotalValueDiscount() <= 0) {

				creditLimit = getCreditLimit(preAprobadosEntity.getId().toString(), preAprobadosEntity.getValidacion());
				requestedAmount = creditLimit;
				creditValue = calculateCreditValue(requestedAmount, TipoCredito.VALUE_LIBRE_INVERSION.getValue());
				calculatorResponse.setMonths(calculateCreditFee(requestedAmount,
						preAprobadosEntity.getTasa().doubleValue(), preAprobadosEntity.getSeguroVidaPor().doubleValue(),
						preAprobadosEntity.getCapacidadPago(), creditValue, preAprobadosEntity.getBankAccount()));

			} else {

				creditLimit = getCreditLimit(preAprobadosEntity.getId().toString(), preAprobadosEntity.getValidacion());
				requestedAmount = userModel.getTotalValueDiscount();
				creditValue = calculateCreditValue(requestedAmount, TipoCredito.VALUE_COMPRA_PRODUCTOS.getValue());
				calculatorResponse.setMonths(calculateCreditFee(creditValue, preAprobadosEntity.getTasa().doubleValue(),
						preAprobadosEntity.getSeguroVidaPor().doubleValue(), preAprobadosEntity.getCapacidadPago(),
						requestedAmount, preAprobadosEntity.getBankAccount()));

			}

			calculatorResponse.setMaxValue(creditLimit);
			calculatorResponse.setRequestedAmount(requestedAmount);

			if (requestedAmount > creditLimit || creditValue > creditLimit) {
				error.setError(true);
				error.setErrorMessage(Response.EXCEDE_CUPO.getValue());
			} else if (calculatorResponse.getMonths().size() <= 0) {
				error.setError(true);
				error.setErrorMessage(Response.EXCEDE_CAPACIDAD_PAGO.getValue());
			}

		}

		calculatorResponse.setErrorResponse(error);

		if (calculatorResponse.getErrorResponse().isError()) {
			calculatorResponse.setMonths(new ArrayList<>());
		}

		return calculatorResponse;
	}

	private double getCreditLimit(String customerIdentificaction, double creditLimit)
			throws URISyntaxException, JsonProcessingException {
		AtomicReference<Double> accumulatedCreditAmount = new AtomicReference<>((double) 0);
		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = UbunpayConstans.getUbunpayEnviromentVariables()
				.get("get-session-management-retrieveCustomerById") + "/?id=" + customerIdentificaction;
		URI uri = new URI(baseUrl);
		ObjectMapper objectMapper = new ObjectMapper();
		Customer customer = objectMapper.readValue(restTemplate.getForObject(uri, String.class), Customer.class);
		if (customer.getTransationsSuccesfulls() != null) {
			customer.getTransationsSuccesfulls().forEach(transationsSuccesfull -> {
				transationsSuccesfull.getTransationsSuccefulls().forEach(creditForMonths -> {
					accumulatedCreditAmount
							.updateAndGet(v -> new Double((double) (v + creditForMonths.getValueToFinance())));
				});
			});
		}
		creditLimit = creditLimit - accumulatedCreditAmount.get();
		return creditLimit;
	}

	private double calculateCreditValue(double valorSolicitado, String tipoCredito) {
		CreditCalculatorRequest credit = new CreditCalculatorRequest();
		double valor = (valorSolicitado * (Variables.ONE.getValue() - credit.getDiscountFund()))
				- credit.getValueStudyCredit() + Variables.VALUE_INCOGNITO.getValue();

		switch (tipoCredito) {
		case "compra productos":
			valor = (valorSolicitado / (Variables.ONE.getValue() - credit.getDiscountFund()))
					+ credit.getValueStudyCredit() + Variables.VALUE_INCOGNITO.getValue();
			break;
		case "libre inversion":
			valor = valor;
			break;
		default:
			valor = valor;
			break;
		}
		return valor;
	}

	@Async
	public void saveInfoCredit(CreditCalculatorResponse creditCalculatorResponse, String token, SessionModel userModel)
			throws URISyntaxException, IOException {
		System.out.println("Inicia doLogAsync: " + System.currentTimeMillis());
		try {
			TimeUnit.SECONDS.sleep(2);
			RestTemplate restTemplate = new RestTemplate();
			final String baseUrl = UbunpayConstans.getUbunpayEnviromentVariables()
					.get("get-session-management-addDataToSession");
			URI uri = new URI(baseUrl);
			userModel = loadUserModelWithCalculate(userModel, creditCalculatorResponse);
			System.out.println("userModel: " + userModel);
			try {
				// Se realiza la actualización del Deal - 07/09/2020 - JESUS SANCHEZ
				if (userModel != null && userModel.getIdDeal() != null) {
					ResultRecordZoho result = zohoIntegrationService.updateDealZoho(userModel,
							ZohoPhasesEnum.SIMULACION_CREDITO.getValue(), null);
				} else {
					ResultRecordZoho result = zohoIntegrationService.createDealZoho(userModel,
							ZohoPhasesEnum.SIMULACION_CREDITO.getValue());
				}
			} catch (Exception e) {
			}

			restTemplate.postForEntity(uri, userModel, String.class);
			System.out.println(baseUrl);
		} catch (InterruptedException e) {
			System.out.println("Error sleep: " + System.currentTimeMillis());
			e.printStackTrace();
		}
		System.out.println("Fin doLogAsync: " + System.currentTimeMillis());
	}

	@Override
	public CreditCalculatorResponse getCalculateValuesForCredit(SessionModel userModel, double value)
			throws URISyntaxException, IOException {

		CreditCalculatorResponse calculatorResponse = new CreditCalculatorResponse();
		ErrorResponse error = new ErrorResponse();
		PreAprobadosEntity preAprobadosEntity = new PreAprobadosEntity();

		if (userModel.getCedulap() != null) {

			preAprobadosEntity = repository.findById(Integer.valueOf(userModel.getCedulap())).orElse(null);
			double creditLimit;
			double creditValue;
			double requestedAmount;

			calculatorResponse.setBankAccount(preAprobadosEntity.getBankAccount());

			creditLimit = getCreditLimit(preAprobadosEntity.getId().toString(), preAprobadosEntity.getValidacion());
			requestedAmount = value;
			creditValue = calculateCreditValue(requestedAmount, TipoCredito.VALUE_LIBRE_INVERSION.getValue());
			calculatorResponse.setMonths(calculateCreditFee(requestedAmount, preAprobadosEntity.getTasa().doubleValue(),
					preAprobadosEntity.getSeguroVidaPor().doubleValue(), preAprobadosEntity.getCapacidadPago(),
					creditValue, preAprobadosEntity.getBankAccount()));

			calculatorResponse.setMaxValue(creditLimit);
			calculatorResponse.setRequestedAmount(requestedAmount);

			if (requestedAmount > creditLimit || creditValue > creditLimit) {
				error.setError(true);
				error.setErrorMessage(Response.EXCEDE_CUPO.getValue());
			} else if (calculatorResponse.getMonths().size() <= 0) {
				error.setError(true);
				error.setErrorMessage(Response.EXCEDE_CAPACIDAD_PAGO.getValue());
			}
		}

		calculatorResponse.setErrorResponse(error);

		if (calculatorResponse.getErrorResponse().isError()) {
			calculatorResponse.setMonths(new ArrayList<>());
		}

		return calculatorResponse;
	}

	private SessionModel loadUserModelWithCalculate(SessionModel userModel,
			CreditCalculatorResponse creditCalculatorResponse) {
		CreditInfo cre = new CreditInfo();
		if (userModel.getCreditInfo() != null) {
			cre = userModel.getCreditInfo();
		}
		System.out.println("creditCalculatorResponse.getBankAccount(): " + creditCalculatorResponse.getBankAccount());
		cre.setMonths(creditCalculatorResponse.getMonths());
		cre.setMaxValue(creditCalculatorResponse.getMaxValue());
		cre.setRequestedAmount(creditCalculatorResponse.getRequestedAmount());
		cre.setBankAccount(creditCalculatorResponse.getBankAccount());
		userModel.setCreditInfo(cre);

		return userModel;
	}

}
