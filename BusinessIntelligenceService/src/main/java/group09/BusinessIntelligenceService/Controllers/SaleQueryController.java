package group09.BusinessIntelligenceService.Controllers;

import group09.BusinessIntelligenceService.Services.SaleInteractiveQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/BIservice")
@RestController
public class SaleQueryController {

    @Autowired
    SaleInteractiveQuery saleInteractiveQuery;

    //this method returns the sales value of a product in real time
    @GetMapping("/totalSalesValue/{productId}")
    public String getProductTotalSalesValue(@PathVariable Long productId)
    {
        return saleInteractiveQuery.getProductTotalSalesValue(productId);
    }

    //this method returns all the product names that have been ordered
    @GetMapping("/productNames")
    List<String> getAllProductNames()
    {
        return saleInteractiveQuery.getProductNames();
    }
}
