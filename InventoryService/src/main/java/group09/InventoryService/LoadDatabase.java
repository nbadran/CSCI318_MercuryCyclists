package group09.InventoryService;

import group09.InventoryService.Entities.Part;
import group09.InventoryService.Entities.Product;
import group09.InventoryService.Repositories.ProductRepository;
import group09.InventoryService.Repositories.PartRepository;
import group09.InventoryService.Services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class LoadDatabase
{
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private final ProductService productService;

    public LoadDatabase(ProductService productService) {
        this.productService = productService;
    }

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository, PartRepository partRepository) {

        return args -> {
            log.info("--- Adding Products to the database...");
            Product product1 = new Product("Bike 1", 250.0, "This product is made in Germany.", 1000);
            Product product2 = new Product("Bike 2", 250.0, "This product is made in Germany.", 1000);
            Product product3 = new Product("Bike 3", 250.0, "This product is made in Germany.", 1000);
            Product product4 = new Product("Bike 4", 250.0, "This product is made in Germany.", 1000);
            Product product5 = new Product("Bike 5", 250.0, "This product is made in Germany.", 1000);
            Product product6 = new Product("Bike 6", 250.0, "This product is made in Germany.", 1000);
            Product product7 = new Product("Bike 7", 250.0, "This product is made in Germany.", 1000);
            Product product8 = new Product("Bike 8", 250.0, "This product is made in Germany.", 1000);
            Product product9 = new Product("Bike 9", 250.0, "This product is made in Germany.", 1000);
            Product product10 = new Product("Bike 10", 250.0, "This product is made in Germany.", 1000);
            log.info("--- Adding Parts to Products in the database...");
            productRepository.save(product1);productRepository.save(product2);productRepository.save(product3);
            productRepository.save(product4);productRepository.save(product5);productRepository.save(product6);
            productRepository.save(product7);productRepository.save(product8);productRepository.save(product9);
            productRepository.save(product10);
            Part p1 = new Part("Tyres and Wheels 1", "German made", 1L,1000, product1);
            Part p2 = new Part("Tyres and Wheels 2", "German made", 2L, 1000, product2);
            Part p3 = new Part("Tyres and Wheels 3", "German made", 3L, 1000, product3);
            Part p4 = new Part("Tyres and Wheels 4", "German made", 4L, 1000, product4);
            Part p5 = new Part("Tyres and Wheels 5", "German made", 5L, 1000, product5);
            Part p6 = new Part("Tyres and Wheels 6", "German made", 6L, 1000, product6);
            Part p7 = new Part("Tyres and Wheels 3", "German made", 3L, 1000, product7);
            Part p8 = new Part("Tyres and Wheels 4", "German made", 4L, 1000, product8);
            Part p9 = new Part("Tyres and Wheels 5", "German made", 5L, 1000, product9);
            Part p10 = new Part("Tyres and Wheels 6", "German made", 6L, 1000, product10);
            partRepository.save(p1);partRepository.save(p2);partRepository.save(p3);
            partRepository.save(p4);partRepository.save(p5);partRepository.save(p6);;partRepository.save(p7);
            partRepository.save(p8);partRepository.save(p9);partRepository.save(p10);
            //testing
            log.info("Preloading " + product1.toString());log.info("Preloading " + product2.toString());log.info("Preloading " + product3.toString());
            log.info("Preloading " + product5.toString());log.info("Preloading " + product5.toString());log.info("Preloading " + product6.toString());
            log.info("Preloading " + product7.toString());log.info("Preloading " + product8.toString());log.info("Preloading " + product9.toString());
            log.info("Preloading " + product10.toString());
            log.info("Preloading " + p1.toString());log.info("Preloading " + p2.toString());log.info("Preloading " + p3.toString());
            log.info("Preloading " + p4.toString());log.info("Preloading " + p5.toString());log.info("Preloading " + p6.toString());
            log.info("Preloading " + p7.toString());log.info("Preloading " + p8.toString());log.info("Preloading " + p9.toString());
            log.info("Preloading " + p10.toString());
            log.info("--- Finished adding Products and Parts to the database!!!");
        };
    }
}