package group09.ProcurementService.Services;

import group09.ProcurementService.Entities.BackOrderSale;
import group09.ProcurementService.Entities.Contact;
import group09.ProcurementService.Entities.Supplier;
import group09.ProcurementService.Repositories.BackOrderSaleRepository;
import group09.ProcurementService.Repositories.ContactRepository;
import group09.ProcurementService.Repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;

@Service //or @Component, but @Service is for more semantics and readability
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final ContactRepository contactRepository;
    private final BackOrderSaleRepository backOrderSaleRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository, ContactRepository contactRepository, BackOrderSaleRepository backOrderSaleRepository, RestTemplate restTemplate) {//}, PartRepository partRepository) {
        this.supplierRepository = supplierRepository;
        this.contactRepository = contactRepository;
        this.backOrderSaleRepository = backOrderSaleRepository;
        //this.partRepository = partRepository;
        this.restTemplate = restTemplate;
    }

    public List<Supplier> getSuppliers() {
        return supplierRepository.findAll();
    }

    public List<Supplier> getSupplier(Long supplierId) {
        return supplierRepository.findAllById(Collections.singleton(supplierId));
    }

    public void addNewSupplier(Supplier supplier) {
        Optional<Supplier> supplierOptional = supplierRepository.findSupplierBySupplierName(supplier.getSupplierName());
        if (supplierOptional.isPresent()) {
            throw new IllegalStateException("Supplier Name Taken.");
        }
        supplierRepository.save(supplier);
    }

    public void deleteSupplier(Long supplierId) {
        //supplierRepository.findById(supplierId);
        boolean exists = supplierRepository.existsById(supplierId);
        if (!exists) {
            throw new IllegalStateException("Supplier with ID " + supplierId + " does not exist.");
        }
        supplierRepository.deleteById(supplierId);
    }

    @Transactional
    public void updateSupplier(Long supplierId, String supplierName, String supplierBase) {
        Supplier supplier = supplierRepository.findBySupplierId(supplierId).orElseThrow(() -> new IllegalStateException("Supplier with id " +
                supplierId + " does not exist."));
        if (supplierName != null && supplierName.length() > 0 && !Objects.equals(supplier.getSupplierName(), supplierName)) {
            supplier.setSupplierName(supplierName);
        }
        if (supplierBase != null && supplierBase.length() > 0 && !Objects.equals(supplier.getSupplierBase(), supplierBase)) {
            supplier.setSupplierBase(supplierBase);
        }
    }

    public void addContact(Long supplierId, Long contactId) {
        Contact contact = contactRepository.findById(contactId).orElseThrow(RuntimeException::new);
        Supplier supplier = supplierRepository.findBySupplierId(supplierId).orElseThrow(RuntimeException::new);
        List<Contact> con = new ArrayList<>();
        if (!supplier.getContacts().isEmpty()) {
            con.addAll(supplier.getContacts());
        }
        con.add(contact);
        supplier.setContacts(con);
        supplierRepository.save(supplier);
    }

    public boolean validateID(@PathVariable Long id) {
        //checks if supplier id exists
        boolean idValid = supplierRepository.existsById(id);
        if (idValid) {
            Optional<Supplier> supplier = supplierRepository.findById(id);
            System.out.println("Supplier with name (  " + supplier.get().getSupplierName() + " ) is VALID");
            return true;
        } else {
            System.out.println("Supplier " + id + " does not exist");
            return false;
        }
    }

    public boolean validateSupplierForBackOrder(Long supplierId, int quantityNeeded) {
        boolean idValid = validateID(supplierId);
        if(idValid)
        {
            System.out.println("YOU HAVE BEEN SUPPLIED WITH THE QUANTITY NEEDED: " + quantityNeeded);
            return true;
        }
        return false;
    }

    // REST REQUEST WORKS - IF KAFKA DOESN'T WORK ( WORST CASE )
    public void receiveFromInventory(Long supplierId, Long partId, int quantityNeeded) {
        System.out.println("--- PROCUREMENT REQUEST SAVED AND CONFIRMED -> ");
        System.out.println("--- SUPPLIER ID    : " + supplierId);
        System.out.println("--- PART ID        : " + partId);
        System.out.println("--- QUANTITY NEEDED: " + quantityNeeded);

        BackOrderSale backOrderSale = new BackOrderSale();
        backOrderSale.setSupplierId(supplierId);
        backOrderSale.setPartId(partId);
        backOrderSale.setQuantityNeeded(quantityNeeded);
        backOrderSaleRepository.save(backOrderSale);
    }
}
