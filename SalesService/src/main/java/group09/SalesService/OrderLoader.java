package group09.SalesService;

import group09.SalesService.Entities.InStoreSale;
import group09.SalesService.Entities.OnlineSale;
import group09.SalesService.Entities.Store;
import group09.SalesService.Repositories.InStoreSaleRepository;
import group09.SalesService.Repositories.OnlineSaleRepository;
import group09.SalesService.Repositories.StoreRepository;
import group09.SalesService.Services.SalesService;
import group09.SalesService.Services.StoreService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
@Transactional
public class OrderLoader{

    private SalesService salesService;
    private StoreService storeService;
    private InStoreSaleRepository inStoreSaleRepository;
    private OnlineSaleRepository onlineSaleRepository;
    private StoreRepository storeRepository;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(OrderLoader.class);
    @Autowired
    private StreamBridge streamBridge;

    public OrderLoader(SalesService salesService, StoreService storeService, StreamBridge streamBridge,
                       InStoreSaleRepository inStoreSaleRepository, OnlineSaleRepository onlineSaleRepository,
                       StoreRepository storeRepository)
    {
        this.salesService = salesService;
        this.storeService = storeService;
        this.streamBridge = streamBridge;
        this.inStoreSaleRepository = inStoreSaleRepository;
        this.onlineSaleRepository = onlineSaleRepository;
        this.storeRepository = storeRepository;
    }

    public void createRandomInStoreOrder() throws Exception{
        int quantity = getRandom();
        int receiptNo = getRandom();
        String dateAndTime = "10/10/2010";
        long storeId = getRandom();
        Store store = storeRepository.findById(storeId).get();
        long productId = getRandom();
        InStoreSale sale = new InStoreSale(productId, quantity, dateAndTime, receiptNo, store);
        inStoreSaleRepository.save(sale);
        log.info("Sale Info: " + sale.toString());
        salesService.createInStoreSale(sale);
    }

    public void createRandomOrder() throws Exception{
        int quantity = getRandom();
        String dateAndTime = "10/10/2010";
        String address = "2 Northfields Ave";
        String customerName = "Customer Name";
        long productId = getRandom();
        OnlineSale sale = new OnlineSale(productId, quantity, dateAndTime, address, customerName);
        onlineSaleRepository.save(sale);
        log.info("Sale Info: " + sale.toString());
        salesService.createOnlineSale(sale);
    }
//*************************************************************
//    @Transactional
//    public void sendOrder(OrderEvent orderEvent){
//        streamBridge.send("order-outbound", orderEvent);
//        log.info("Order sent: " + orderEvent.toString());
//    }
//*************************************************************
@Bean
CommandLineRunner initDatabase(StoreRepository storeRepository, InStoreSaleRepository inStoreSaleRepository,
                               OnlineSaleRepository onlineSaleRepository) {

    return args -> {
        log.info("Adding Stores to the database...");

        List<InStoreSale> sales1 = new ArrayList<>();List<InStoreSale> sales2 = new ArrayList<>();
        List<InStoreSale> sales3 = new ArrayList<>();List<InStoreSale> sales4 = new ArrayList<>();
        List<InStoreSale> sales6 = new ArrayList<>();List<InStoreSale> sales9 = new ArrayList<>();
        List<InStoreSale> sales7 = new ArrayList<>();List<InStoreSale> sales10 = new ArrayList<>();
        List<InStoreSale> sales5 = new ArrayList<>();List<InStoreSale> sales8 = new ArrayList<>();

        Store store1 = new Store("Store Address 1", "Store Manager 1", "Store 1", sales1);
        Store store2 = new Store("Store Address 2", "Store Manager 2", "Store 2", sales2);
        Store store3 = new Store("Store Address 3", "Store Manager 3", "Store 3", sales3);
        Store store4 = new Store("Store Address 4", "Store Manager 4", "Store 4", sales4);
        Store store5 = new Store("Store Address 5", "Store Manager 5", "Store 5", sales5);
        Store store6 = new Store("Store Address 6", "Store Manager 6", "Store 6", sales6);
        Store store7 = new Store("Store Address 7", "Store Manager 7", "Store 7", sales7);
        Store store8 = new Store("Store Address 8", "Store Manager 8", "Store 8", sales8);
        Store store9 = new Store("Store Address 9", "Store Manager 9", "Store 9", sales9);
        Store store10 = new Store("Store Address 10", "Store Manager 10", "Store 10", sales10);
        storeRepository.save(store1);storeRepository.save(store2);storeRepository.save(store3);
        storeRepository.save(store4);storeRepository.save(store5);storeRepository.save(store6);
        storeRepository.save(store7);storeRepository.save(store8);storeRepository.save(store9);
        storeRepository.save(store10);

        log.info("Adding orders to the database...");

        //every 2 seconds a random inStore/online order is created
        try {
            while (!Thread.currentThread().isInterrupted()) {
                log.info("------------ ONLINE ORDER ------------");
                createRandomOrder();
                Thread.sleep(2000);
                log.info("------------ IN-STORE ORDER ------------");
                createRandomInStoreOrder();
                Thread.sleep(2000);
            }
        } catch (InterruptedException ignored) {
        }
    };
    }

    public int getRandom() {
        Random random = new Random();
        return random.nextInt(10 - 1) + 1;
    }
}