package group09.ProcurementService;

import group09.ProcurementService.Entities.jsonWrapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class ProcurementServiceApplication {

	private static final Logger log = LoggerFactory.getLogger(ProcurementServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProcurementServiceApplication.class, args);
	}
	@Bean
	public Consumer<jsonWrapper> consume() {
		return input -> log.info(input.toString());
	}
}
