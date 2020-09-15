package ubunpay.credit.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;

import ubunpay.commons.zoho.application.service.IZohoIntegrationService;
import ubunpay.commons.zoho.application.service.impl.ZohoIntegrationService;

@SpringBootApplication
@EntityScan({"ubunpay.commons"})
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public IZohoIntegrationService getZohoIntegrationServiceInstance() {
		return new ZohoIntegrationService();    	
    }

    @Bean
    public IZohoIntegrationService getZohoIntegrationServiceInstance() {
		return new ZohoIntegrationService();    	
    }
    
}
