package group09.ProcurementService;


import group09.ProcurementService.Entities.Contact;
import group09.ProcurementService.Entities.Supplier;
import group09.ProcurementService.Repositories.ContactRepository;
import group09.ProcurementService.Repositories.SupplierRepository;
import group09.ProcurementService.Services.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan
public class LoadDatabase
{
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private final SupplierService supplierService;

    public LoadDatabase(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Bean
    CommandLineRunner initDatabase(SupplierRepository supplierRepository, ContactRepository contactRepository) {

        return args -> {
            log.info("--- Adding Suppliers to the database...");
            Supplier supplier1 = new Supplier("Trek", "Wollongong, Australia");
            Supplier supplier2 = new Supplier("Pedal Pals", "Wollongong, Australia");
            Supplier supplier3 = new Supplier("Bike Nation", "Wollongong, Australia");
            Supplier supplier4 = new Supplier("Bike Boys", "Wollongong, Australia");
            Supplier supplier5 = new Supplier("Super Bikes", "Wollongong, Australia");
            Supplier supplier6 = new Supplier("Careful Cycles", "Wollongong, Australia");
            supplierRepository.save(supplier1);supplierRepository.save(supplier2);supplierRepository.save(supplier3);
            supplierRepository.save(supplier4);supplierRepository.save(supplier5);supplierRepository.save(supplier6);
            log.info("--- Adding Contacts to Suppliers in the database...");
            Contact contact1 = new Contact("Contact 1", "0424144553", "email@uow.edu.au", "Employee", supplier1);
            Contact contact2 = new Contact("Contact 2", "0424144553", "email@uow.edu.au", "Employee", supplier2);
            Contact contact3 = new Contact("Contact 3", "0424144553", "email@uow.edu.au", "Employee", supplier3);
            Contact contact4 = new Contact("Contact 4", "0424144553", "email@uow.edu.au", "Employee", supplier4);
            Contact contact5 = new Contact("Contact 5", "0424144553", "email@uow.edu.au", "Employee", supplier5);
            Contact contact6 = new Contact("Contact 6", "0424144553", "email@uow.edu.au", "Employee", supplier6);
            contactRepository.save(contact1);contactRepository.save(contact2);contactRepository.save(contact3);
            contactRepository.save(contact4);contactRepository.save(contact5);contactRepository.save(contact6);
            //testing
            log.info("Preloading " + supplier1.toString());log.info("Preloading " + supplier2.toString());
            log.info("Preloading " + supplier3.toString());log.info("Preloading " + supplier4.toString());
            log.info("Preloading " + supplier5.toString());log.info("Preloading " + supplier6.toString());
            log.info("Preloading " + contact1.toString());log.info("Preloading " + contact2.toString());
            log.info("Preloading " + contact4.toString());log.info("Preloading " + contact4.toString());
            log.info("Preloading " + contact5.toString());log.info("Preloading " + contact6.toString());
            log.info("--- Finished adding Suppliers and Contacts to the database!!!");
        };
    }
}

