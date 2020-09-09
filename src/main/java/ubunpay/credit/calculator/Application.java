package ubunpay.credit.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ubunpay.commons.zoho.application.service.IZohoIntegrationService;
import ubunpay.commons.zoho.application.service.impl.ZohoIntegrationService;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public IZohoIntegrationService getZohoIntegrationServiceInstance() {
		return new ZohoIntegrationService();    	
    }

}
