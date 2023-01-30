package group09.SalesService.Services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import group09.SalesService.Entities.*;
import group09.SalesService.Repositories.OnlineSaleRepository;
import group09.SalesService.Repositories.SaleRepository;
import group09.SalesService.Repositories.InStoreSaleRepository;
import group09.SalesService.Repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesService {
    private final SaleRepository saleRepository;
    private final InStoreSaleRepository inStoreSaleRepository;
    private final OnlineSaleRepository onlineSaleRepository;
    private final StoreRepository storeRepository;
    private final StreamBridge streamBridge;

    @Autowired
    private final RestTemplate restTemplate;

    public SalesService(SaleRepository saleRepository, InStoreSaleRepository inStoreSaleRepository, OnlineSaleRepository onlineSaleRepository, StoreRepository storeRepository, StreamBridge streamBridge, RestTemplate restTemplate) {
        this.saleRepository = saleRepository;
        this.inStoreSaleRepository = inStoreSaleRepository;
        this.onlineSaleRepository = onlineSaleRepository;
        this.storeRepository = storeRepository;
        this.streamBridge = streamBridge;
        this.restTemplate = restTemplate;
    }

    public List<Sale> getSales() {
        return saleRepository.findAll();
    }

    public List<Sale> getSale(Long id) {
        return saleRepository.findAllById(Collections.singleton(id));
    }

    public Optional<Sale> updateSale(Sale newSale, Long id) {
        return saleRepository.findById(id)
                .map(sale -> {
                    sale.setQuantity(newSale.getQuantity());
                    sale.setDateAndTime(newSale.getDateAndTime());
                    sale.setSaleId(newSale.getSaleId());
                    sale.setProductId(newSale.getProductId());
                    return saleRepository.save(sale);
                });
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }

    //---------------------------- ( Use Case #8 ) ---------------------------------------------------------------------
    public void addProductToSale(Long saleId, Long productId) {
        //create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        //validation - make an HTTP GET request to check if there is a product with given ID in the Inventory Microservice
        ResponseEntity<Boolean> validateProduct = restTemplate.exchange("http://localhost:8080/product/productsValidate/" + productId.toString(),
                HttpMethod.GET, request, Boolean.class);
        //if supplier is valid
        if (validateProduct.getBody() == Boolean.TRUE) {
            System.out.println("Product with ID ( " + productId + " ) is VALID");
            Sale sale = saleRepository.findById(saleId).orElseThrow(RuntimeException::new);
            sale.setProductId(productId);
            saleRepository.save(sale);
            System.out.println("Product with ID ( " + productId + " ) has been successfully added to Sale with ID ( " + saleId + " )");
        } else {
            throw new IllegalStateException("Product with ID ( " + productId + " ) does not exist");
        }
    }

    //---------------------------- ( Use Case #8 ) ---------------------------------------------------------------------
    //we pass saleId to the method -> instance of sale is created that is mapped to
    //sale with same saleId in saleRepository is mapped to an instance of Sale
    //then, we get the productId of the sale and use HTTP REST Request to get the product
    //that is related to the sale
    public ResponseEntity<Object> lookUpProductBySale(Long saleId) {
        Sale sale = saleRepository.findById(saleId).orElseThrow(RuntimeException::new);
        Long productId = sale.getProductId();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Object> object = restTemplate.exchange("http://localhost:8080/product/" + productId,
                HttpMethod.GET, request, Object.class);
        return object;
    }

    //---------------------------- ( SALE VALIDATE ) -------------------------------------------------------------------

    public boolean validateID(Long saleId) {
        boolean idValid = inStoreSaleRepository.existsById(saleId);
        if (idValid)
        {
            InStoreSale sale = inStoreSaleRepository.findById(saleId).orElseThrow(RuntimeException::new);
            System.out.println("Sale with ID ( " + sale.getSaleId() + " ) is VALID");
            return true;
        }
        System.out.println("Sale with ID ( " + saleId + " ) does not exist");
        return false;
    }

    //---------------------------- ( Use Case #7 CREATE SALE METHODS ) -------------------------------------------------
    //ONLY USED FOR TESTING PURPOSES
    public Sale createSale(Sale sale) {
        return saleRepository.save(sale);
    }

    //CUTOMERS USE THE BELOW METHODS
    //CREATE IN STORE SALE ****************************************************************************************************************************************

    public String createInStoreSale(InStoreSale sale)
    {
        //Long storeId = sale.getStoreId();
        Store store = sale.getStore();
        //Store store = storeRepository.findById(storeId).orElseThrow(RuntimeException::new);
        List<InStoreSale> listOfSales = new ArrayList<>();
        Long productId = sale.getProductId();
        // create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        // make an HTTP GET request to validate Product
        ResponseEntity<Boolean> validateProduct = restTemplate.exchange("http://localhost:8080/product/productsValidate/" + productId.toString(),
                HttpMethod.GET, request, Boolean.class);
        if(Boolean.TRUE.equals(validateProduct.getBody()))
        {
            System.out.println("Product ID is VALID, we are now checking quantity of both PRODUCT and PART");
            if( saveOrder(productId, sale.getQuantity()) )
            {
                inStoreSaleRepository.save(sale);
//                if (!store.getSales().isEmpty()) {
//                    listOfSales.addAll(store.getSales());
//                }
                listOfSales.add(sale);
                store.setSales(listOfSales);
                storeRepository.save(store);

                //----------------------------------------------------
                ResponseEntity<String> name = restTemplate.exchange("http://localhost:8080/product/getProductName/" + productId.toString()
                        , HttpMethod.GET, request, String.class);
                String productName = name.getBody();
                System.out.println("PRODUCT NAME = " + productName);
                ResponseEntity<Double> price = restTemplate.exchange("http://localhost:8080/product/getProductPrice/" + productId.toString()
                        , HttpMethod.GET, request, Double.class);
                double productPrice = price.getBody();
                System.out.println("PRODUCT PRICE = " + productPrice);
                //----------------------------------------------------
                SaleEvent saleEvent = new SaleEvent(productId, productName, productPrice, sale.getQuantity());
                streamBridge.send("order-event-outbound", saleEvent);
                //----------------------------------------------------
                System.out.println("--- ORDER CONFIRMED");
                return "--- ORDER CONFIRMED";
            }
            else
            {
                System.out.println("--- ORDER IS NOT CONFIRMED, BACK ORDER OPTION AVAILABLE");
                return "--- ORDER IS NOT CONFIRMED, BACK ORDER OPTION AVAILABLE";
                //back order option
            }
        }else
        {
            System.out.println("Product ID is NOT VALID");
            return "Product ID is NOT VALID";
        }
    }

    //*************************************************************************************************************************************************************
    //CREATE ONLINE SALE ******************************************************************************************************************************************
    public String createOnlineSale(OnlineSale sale) {
        Long productId = sale.getProductId();
        // create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        // make an HTTP GET request to validate Product
        ResponseEntity<Boolean> validateProduct = restTemplate.exchange("http://localhost:8080/product/productsValidate/" + productId.toString()
                , HttpMethod.GET, request, Boolean.class);
        if(Boolean.TRUE.equals(validateProduct.getBody()))
        {
            System.out.println("Product ID is VALID, we are now checking quantity of both PRODUCT and PART");
            if( saveOrder( productId, sale.getQuantity()) )
            {
                onlineSaleRepository.save(sale);

                //----------------------------------------------------
                ResponseEntity<String> name = restTemplate.exchange("http://localhost:8080/product/getProductName/" + productId.toString()
                        , HttpMethod.GET, request, String.class);
                String productName = name.getBody();
                System.out.println("PRODUCT NAME = " + productName);
                ResponseEntity<Double> price = restTemplate.exchange("http://localhost:8080/product/getProductPrice/" + productId.toString()
                        , HttpMethod.GET, request, Double.class);
                double productPrice = price.getBody();
                System.out.println("PRODUCT PRICE = " + productPrice);
                //----------------------------------------------------
                SaleEvent saleEvent = new SaleEvent(productId, productName, productPrice, sale.getQuantity());
                streamBridge.send("order-event-outbound", saleEvent);
                //----------------------------------------------------

                System.out.println("--- ORDER CONFIRMED");
                return "--- ORDER CONFIRMED";
            }
            else
            {
                System.out.println("--- ORDER IS NOT CONFIRMED, PLEASE APPLY BACK ORDER OPTION");
                return "--- ORDER IS NOT CONFIRMED, PLEASE APPLY BACK ORDER OPTION";
            }
        }else
        {
            System.out.println("--- ORDER IS NOT CONFIRMED, PRODUCT ID IS NOT VALID");
            return "--- ORDER IS NOT CONFIRMED, PRODUCT ID IS NOT VALID";
        }
    }

    //*************************************************************************************************************************************************************
    //SAVE ORDER FOR BOTH IN STORE AND ONLINE SALE ****************************************************************************************************************

    public boolean saveOrder(Long productId, int quantity){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Boolean> productStockQuantity = restTemplate.exchange("http://localhost:8080/product/productQuantityById/" + productId.toString() + "/" + quantity,
                HttpMethod.GET, request, Boolean.class);
        System.out.println("RESPONSE FROM INVENTORY --- ( " + productStockQuantity.getBody() + " ) ---");
        if(Boolean.TRUE.equals(productStockQuantity.getBody()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //*************************************************************************************************************************************************************
    //BACK ORDER SALE FOR IN STORE SALE ***************************************************************************************************************************

    public String createBackOrderInStoreSale(InStoreSale inStoreSale) {
        //Long storeId = inStoreSale.getStoreId();
        Store store = inStoreSale.getStore();
        //Store store = storeRepository.findById(storeId).orElseThrow(RuntimeException::new);
        List<InStoreSale> listOfInStoreSales = new ArrayList<>();
        boolean saveOrder = saveOrder(inStoreSale.getProductId(), inStoreSale.getQuantity());
        if(Boolean.FALSE.equals(saveOrder))
        {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<Boolean> requestInventory = restTemplate.exchange("http://localhost:8080/product/procurementRequest/" +
                            inStoreSale.getProductId() + "/" + inStoreSale.getQuantity(),
                    HttpMethod.GET, request, Boolean.class);
            if(Boolean.TRUE.equals(requestInventory.getBody()))
            {
                saleRepository.save(inStoreSale);
                System.out.println("--- BACK ORDER CONFIRMED ----");
//                if (!store.getSales().isEmpty()) {
//                    listOfInStoreSales.addAll(store.getSales());
//                }
                listOfInStoreSales.add(inStoreSale);
                store.setSales(listOfInStoreSales);
                inStoreSale.setStore(store);
                storeRepository.save(store);
                return "--- BACK ORDER CONFIRMED ----";
            }
            else
            {
                System.out.println("--- BACK ORDER NOT CONFIRMED ----");
                return "--- BACK ORDER NOT CONFIRMED ----";
            }
        }
        else
        {
            saleRepository.save(inStoreSale);
            if (!store.getSales().isEmpty()) {
                listOfInStoreSales.addAll(store.getSales());
            }
            listOfInStoreSales.add(inStoreSale);
            store.setSales(listOfInStoreSales);
            storeRepository.save(store);
             return "--- BACK ORDER CONFIRMED ----";
        }
    }

    //*************************************************************************************************************************************************************
    //BACK ORDER SALE FOR ONLINE SALE *****************************************************************************************************************************

    public String createBackOrderOnlineSale(OnlineSale onlineSale) {
        boolean saveOrder = saveOrder(onlineSale.getProductId(), onlineSale.getQuantity());
        if(Boolean.FALSE.equals(saveOrder))
        {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<Boolean> requestInventory = restTemplate.exchange("http://localhost:8080/product/procurementRequest/" +
                            onlineSale.getProductId().toString() + "/" + onlineSale.getQuantity(),
                    HttpMethod.GET, request, Boolean.class);
            if(Boolean.TRUE.equals(requestInventory.getBody()))
            {
                saleRepository.save(onlineSale);
                System.out.println("--- BACK ORDER CONFIRMED ----");
                return "--- BACK ORDER CONFIRMED ----";
            }
            else
            {
                System.out.println("--- BACK ORDER NOT CONFIRMED ----");
                return "--- BACK ORDER NOT CONFIRMED ----";
            }
        }
        else
        {
             saleRepository.save(onlineSale);
             return "--- BACK ORDER CONFIRMED ----";
        }
    }
}