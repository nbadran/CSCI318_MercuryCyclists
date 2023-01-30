package group09.InventoryService.Controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PartConfig {

    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }
}
