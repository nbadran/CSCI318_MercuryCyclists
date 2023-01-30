package group09.BusinessIntelligenceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BusinessServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(BusinessServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BusinessServiceApplication.class, args);
    }

}
