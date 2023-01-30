package group09.SalesService.Services;

import group09.SalesService.Entities.InStoreSale;
import group09.SalesService.Entities.Store;
import group09.SalesService.Repositories.InStoreSaleRepository;
import group09.SalesService.Repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final InStoreSaleRepository inStoreSaleRepository;

    @Autowired
    private final RestTemplate restTemplate;

    public StoreService(StoreRepository storeRepository, InStoreSaleRepository inStoreSaleRepository, RestTemplate restTemplate) {//, RestTemplate restTemplate) {
        this.storeRepository = storeRepository;
        this.inStoreSaleRepository = inStoreSaleRepository;
        this.restTemplate = restTemplate;
    }

    public List<Store> getStores() {
        return storeRepository.findAll();
    }

    public List<Store> getStore(Long id) {
        return storeRepository.findAllById(Collections.singleton(id));
    }

    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    public Optional<Store> updateStore(Store newStore, Long id) {
        return storeRepository.findById(id)
                .map(store -> {
                    store.setAddress(newStore.getAddress());
                    store.setManager(newStore.getManager()); //new addition
                    store.setSales(newStore.getSales());
                    return storeRepository.save(store);
                });
    }

    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }

    //Use Case #9
    public void addSaleToStore(Long saleId, Long storeId) {
        //create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);
        //make an HTTP GET request to check if there is a sale with given ID in the Sales Microservice
        ResponseEntity<Boolean> validateSale = restTemplate.exchange("http://localhost:8082/sale/saleValidate/" + saleId.toString(),
                HttpMethod.GET, request, Boolean.class);
        //if sale is valid
        if (Boolean.TRUE.equals(validateSale.getBody())) {
            System.out.println("Sale with ID ( " + saleId + " ) is VALID");
            InStoreSale sale;
            sale = inStoreSaleRepository.findById(saleId).orElseThrow(RuntimeException::new);////////////
            System.out.println("sale : " + sale.getSaleId());
            Store store = storeRepository.findById(storeId).orElseThrow(RuntimeException::new);
            System.out.println("store : " + store.getStoreId());
            List<InStoreSale> sales = new ArrayList<>();
            if (!store.getSales().isEmpty()) {
                sales.addAll(store.getSales());
            }
            sales.add(sale);
            store.setSales(sales);
            storeRepository.save(store);
            System.out.println("Store Sales: " + store.getSales());
            System.out.println("Sale with ID ( " + saleId + " ) has been successfully added to store with ID " + store.getStoreId());
        } else {
            throw new IllegalStateException("Sale with ID ( " + saleId + " ) does not exist");
        }
    }

    //Use Case #9
    public List<InStoreSale> lookUpSalesByStore(Long storeId) {
        boolean exists = storeRepository.existsById(storeId);
        if(!exists){
            throw new IllegalStateException("Store with ID " + storeId + " does not exist");
        }
        Store store = storeRepository.findById(storeId).orElseThrow(RuntimeException::new);
        System.out.println("Store Id: " + store.getStoreId());
        System.out.println("Store sales: " + store.getSales());
        return store.getSales();

    }
}
