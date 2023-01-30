package group09.SalesService.Controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SaleConfig {
    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }
}
