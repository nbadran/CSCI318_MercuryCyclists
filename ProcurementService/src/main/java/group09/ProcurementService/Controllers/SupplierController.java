package group09.ProcurementService.Controllers;

import group09.ProcurementService.Entities.Supplier;
import group09.ProcurementService.Services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/suppliers") //localhost
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping //this method returns list of suppliers
    public List<Supplier> getSuppliers()
    {
        return supplierService.getSuppliers();
    }

    @GetMapping(path = "{supplierId}") //this method returns supplier with given supplierId
    public List<Supplier> getSupplier(@PathVariable("supplierId") Long supplierId)
    {
        return supplierService.getSupplier(supplierId);
    }

    @PostMapping //POST is used when you want to add new resources to your database
    public void registerNewSupplier(@RequestBody Supplier supplier) //it takes from the requestBody and maps it to the supplier
    {
        supplierService.addNewSupplier(supplier);
    }

    @DeleteMapping(path = "{supplierId}")
    public void deleteSupplier(@PathVariable("supplierId") Long supplierId)
    {
        supplierService.deleteSupplier(supplierId);
    }

    @PutMapping(path = "{supplierId}")
    public void updateSupplier(
            @PathVariable("supplierId") Long supplierId,
            @RequestParam(required = false) String supplierName,
            @RequestParam(required = false) String supplierBase)
    {
        supplierService.updateSupplier(supplierId, supplierName, supplierBase);
    }

    @PutMapping(path = "{supplierId}/supplierContacts/{contactId}")
    public void AddContactToSupplier(@PathVariable("supplierId") Long supplierId,
                                     @PathVariable("contactId") Long contactId)
    {
        supplierService.addContact(supplierId, contactId);
    }

    //validate suppliers
    @GetMapping("/suppliersValidate/{supplierId}")
    public boolean validateSupplier(@PathVariable Long supplierId){
        return supplierService.validateID(supplierId);
    }


    //procurementRequest - THIS METHOD SAVES AND CONFIRMS THE REQUEST FROM INVENTORY
    // REST REQUEST - IF KAFKA DOESN'T WORK
    @GetMapping("/procurementRequest/{supplierId}/{partId}/{quantityNeeded}")
    public void receiveFromInventory(@PathVariable Long supplierId, @PathVariable Long partId, @PathVariable int quantityNeeded){
         supplierService.receiveFromInventory(supplierId, partId, quantityNeeded);
    }
}


