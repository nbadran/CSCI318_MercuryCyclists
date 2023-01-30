package group09.InventoryService.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import group09.InventoryService.Entities.BackOrder;
import group09.InventoryService.Entities.Part;
import group09.InventoryService.Entities.Product;
import group09.InventoryService.Entities.jsonWrapper;
//import group09.InventoryService.KafkaConfig.KafkaProducer;
import group09.InventoryService.Repositories.PartRepository;
import group09.InventoryService.Repositories.ProductRepository;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PartRepository partRepository;
    private final StreamBridge streamBridge;
    //private final KafkaProducer kafkaProducer;

    public ProductService(ProductRepository productRepository, PartRepository partRepository, StreamBridge streamBridge){
        this.productRepository = productRepository;
        this.partRepository = partRepository;
        this.streamBridge = streamBridge;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProduct(Long id) {
        return productRepository.findAllById(Collections.singleton(id));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Product newProduct, Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setProductName(newProduct.getProductName());
                    product.setComment(newProduct.getComment());
                    product.setPrice(newProduct.getPrice());
                    product.setStockQuantity(newProduct.getStockQuantity());
                    product.setParts(newProduct.getParts());
                    return productRepository.save(product);
                });
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void addPart(Long productId, Long partId) {
        Part part = partRepository.findById(partId).orElseThrow(RuntimeException::new);
        Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
        List<Part> parts = new ArrayList<>();
        if (!product.getParts().isEmpty()) {
            parts.addAll(product.getParts());
        }
        part.setProduct(product);
        parts.add(part);
        product.setParts(parts);
        productRepository.save(product);
    }

    //Look up all parts by product
    public List<Part> getParts(Long id) {
        boolean exists = productRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Product with ID " + id + " does not exist");
        }
        Product product = productRepository.findById(id).orElseThrow(RuntimeException::new);
        return product.getParts();
    }

    public boolean validateID(Long productId) {
        boolean idValid = productRepository.existsById(productId);
        if (idValid) {
            Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
            System.out.println("Product Name with name ( " + product.getProductName() + " ) and ID ( " + product.getProductId() + " ) is VALID");
            return true;
        } else {
            System.out.println("Product with ID ( " + productId + " ) does not exist");
            return false;
        }
    }

    public boolean validateProductParts(Long productId) {
        boolean idValid = productRepository.existsById(productId);
        if (idValid) {
            Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
            if(product.getParts().isEmpty())
            {
                System.out.println("Product with ID ( " + productId + " ) has parts: " + product.getParts());
                return false;
            }
            return true;
        } else {
            System.out.println("Product with ID ( " + productId + " ) does not exist");
            return false;
        }
    }

    public boolean productQuantityById(Long productId, int quantityNeeded) {
            Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
            List<Part> productParts = product.getParts();
            if(product.getStockQuantity() >= quantityNeeded) {
                int currentStockQuantity = product.getStockQuantity();
                product.setStockQuantity(currentStockQuantity - quantityNeeded);
                productRepository.save(product);
                System.out.println("--- ( PRODUCT STOCK QUANTITY BEFORE ORDER = " + currentStockQuantity + " ) ---");
                System.out.println("--- ( PRODUCT STOCK QUANTITY AFTER ORDER  = " + product.getStockQuantity() + " ) ---");
            }
            else
            {
                for(Part p : productParts)
                {
                    Part part = partRepository.findById(p.getPartId()).orElseThrow(RuntimeException::new);
                    if(part.getQuantity() < quantityNeeded)
                    {
                        System.out.println("--- ( BOTH PRODUCT AND PARTS OF PRODUCT ARE OUT OF STOCK ) ---");
                        return false;
                    }
                }
            }
            for(Part p : productParts)
            {
                Part part = partRepository.findById(p.getPartId()).orElseThrow(RuntimeException::new);
                int currentPartStock = part.getQuantity();
                part.setQuantity(currentPartStock - quantityNeeded);
                partRepository.save(part);
                System.out.println("--- ( PART STOCK QUANTITY BEFORE ORDER = " + currentPartStock + " ) ---");
                System.out.println("--- ( PART STOCK QUANTITY AFTER ORDER  = " + part.getQuantity() + " ) ---");
            }
        System.out.println("--- ( THE QUANTITY NEEDED FROM CUSTOMER IS OK ---> ORDER IS CONFIRMED ) ---");
        return true;
    }
    /*
    //REST REQUEST BETWEEN INVENTORY AND PROCUREMENT WORKS ( WORST CASE )
    public boolean procurementRequest(Long productId, int quantityNeeded){
        boolean idValid = productRepository.existsById(productId);
        if (idValid) {
            Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
            System.out.println("Product Name with name ( " + product.getProductName() + " ) and ID ( " + product.getProductId() + " ) is VALID");
            List<Part> productParts = product.getParts();
            for (Part p : productParts) {
                Long supplierId = p.getSupplierId();
                System.out.println("--- PROCUREMENT REQUEST CONFIRMED FOR:");
                System.out.println("--- SUPPLIER ID    : " + supplierId);
                System.out.println("--- PART ID        : " + p.getPartId());
                System.out.println("--- QUANTITY NEEDED: " + quantityNeeded);
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                HttpEntity<String> request = new HttpEntity<>(headers);
                ResponseEntity<Void> validateSupplierForBackOrder = restTemplate.exchange("http://localhost:8081/suppliers/procurementRequest/" +
                        supplierId + "/" + p.getPartId() + "/" + quantityNeeded, HttpMethod.GET, request, Void.class);
                partRepository.save(p);
                return true;
            }
        }
            System.out.println("Product with ID ( " + productId + " ) is NOT VALID");
            return false;
    }*/

    //USING KAFKA
    public boolean procurementRequestUsingKafka(Long productId, int quantityNeeded) throws JsonProcessingException {
        boolean idValid = productRepository.existsById(productId);
        if (idValid) {
            Product product = productRepository.findById(productId).orElseThrow(RuntimeException::new);
            System.out.println("Product Name with name ( " + product.getProductName() + " ) and ID ( " + product.getProductId() + " ) is VALID");
            List<Part> productParts = product.getParts();

            List<BackOrder> backOrderList = new ArrayList<>();
            jsonWrapper jsonWrapper = new jsonWrapper();

            for (Part p : productParts) {
                backOrderList.add(new BackOrder(p.getSupplierId(), p.getPartId(), quantityNeeded));
                System.out.println("--- PROCUREMENT REQUEST CONFIRMED FOR:");
                System.out.println("--- SUPPLIER ID    : " + p.getSupplierId());
                System.out.println("--- PART ID        : " + p.getPartId());
                System.out.println("--- QUANTITY NEEDED: " + p.getQuantity());
            }
            jsonWrapper.setBackOrderList(backOrderList);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(jsonWrapper);
            //testing purposes
            System.out.println(json);
            System.out.println(json.getClass().getName());
            streamBridge.send("appliance-outbound", json);
            return true;
        }
        System.out.println("Product with ID ( " + productId + " ) is NOT VALID");
        return false;
    }

    public String getProductName(Long productId) {
        Product product = productRepository.findById(productId).get();
        return product.getProductName();
    }

    public double getProductPrice(Long productId) {
        Product product = productRepository.findById(productId).get();
        return product.getPrice();
    }
}