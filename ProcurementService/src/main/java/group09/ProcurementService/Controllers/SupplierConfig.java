package group09.ProcurementService.Controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SupplierConfig {

    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

}
