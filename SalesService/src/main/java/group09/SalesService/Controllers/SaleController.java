package group09.SalesService.Controllers;

import group09.SalesService.Entities.InStoreSale;
import group09.SalesService.Entities.OnlineSale;
import group09.SalesService.Entities.Sale;
import group09.SalesService.Services.SalesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@RequestMapping(path = "/sale")
public class SaleController {
    private final SalesService salesService;

    public SaleController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping
    List<Sale> getSales(){
        return salesService.getSales();
    }

    @GetMapping(path = "{id}")
    List<Sale> getSale(@PathVariable("id") Long id)
    {
        return salesService.getSale(id);
    }

    @PutMapping(path = "{id}")
    public Optional<Sale> updateSale(@RequestBody Sale newSale, @PathVariable("id") Long id)
    {
        return salesService.updateSale(newSale, id);
    }

    @DeleteMapping(path = "{id}")
    public void deleteSale(@PathVariable("id") Long id)
    {
        salesService.deleteSale(id);
    }

    //Use Case #8 - Look up products info by sale
    @PutMapping("/addProductToSale/{productId}/{saleId}")
    public void addProductToSale(@PathVariable("productId") Long productId, @PathVariable("saleId") Long saleId)//(@RequestParam(value="partId") Long partId, @RequestParam(value="supplierId") Long supplierId)
    {
        salesService.addProductToSale(saleId, productId);
    }
    //Use Case #8 - Look up products info by sale
    @GetMapping(path = "lookUpProductBySale/{saleId}")
    public ResponseEntity<Object> lookUpProductBySale(@PathVariable("saleId") Long saleId)
    {
        return salesService.lookUpProductBySale(saleId);
    }

    //Use Case #7 - Create Sale -> following the sequence diagram
    //Customers use the createInStoreSale OR createOnlineSale
    @PostMapping
    Sale createSale(@RequestBody Sale sale)
    {
            return salesService.createSale(sale);
    }

    @PostMapping("/inStoreSale")
    String createInStoreSale(@RequestBody InStoreSale inStoreSale)
    {
        return salesService.createInStoreSale(inStoreSale);
    }

    @PostMapping("/onlineSale")
    String createOnlineSale(@RequestBody OnlineSale onlineSale)
    {
        return salesService.createOnlineSale(onlineSale);
    }

    //validate sale
    @GetMapping("/saleValidate/{saleId}")
    public boolean validateSale(@PathVariable Long saleId){
        return salesService.validateID(saleId);
    }

    //accept back order option for inStoreSale
    @PostMapping("/createBackOrderSale/inStoreSale")
    String createBackOrderInStoreSale(@RequestBody InStoreSale inStoreSale){
        return salesService.createBackOrderInStoreSale(inStoreSale);
    }

    //accept back order option for OnlineSale
    @PostMapping("/createBackOrderSale/OnlineSale")
    String createBackOrderInStoreSale(@RequestBody OnlineSale onlineSale){
        return salesService.createBackOrderOnlineSale(onlineSale);
    }
}