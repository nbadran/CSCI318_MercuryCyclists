package group09.BusinessIntelligenceService.Services;

import group09.BusinessIntelligenceService.Entities.SaleEvent;
import org.springframework.stereotype.Service;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;

import java.util.ArrayList;
import java.util.List;

@Service
public class SaleInteractiveQuery {

    private final InteractiveQueryService interactiveQueryService;

    @Autowired
    public SaleInteractiveQuery(InteractiveQueryService interactiveQueryService) {
        this.interactiveQueryService = interactiveQueryService;
    }

    public String  getProductTotalSalesValue (Long productId) {
        Long totalSalesValue = 0L;

        KeyValueIterator<String, SaleEvent> all = productSaleEventStore().all();
        while (all.hasNext()) {
            SaleEvent saleEvent = all.next().value;
            Long table_productId = saleEvent.getProductId();
            long quantity = saleEvent.getQuantity();
            long price = saleEvent.getProductPrice();
            if (table_productId == (productId)) {
                totalSalesValue += (quantity * price);
            }
        }
        String result = "Total Sales Value = " + totalSalesValue.toString();
        return result;
    }

    public ReadOnlyKeyValueStore<String, SaleEvent> productSaleEventStore() {
        return this.interactiveQueryService.getQueryableStore(SaleStreamProcessing.PRODUCT_STATE_STORE,
                QueryableStoreTypes.keyValueStore());
    }

    public List<String> getProductNames() {
        List<String> productList = new ArrayList<>();
        KeyValueIterator<String, Long> all = keyValueStore().all();
        while (all.hasNext()) {
            String next = all.next().key;
            next = next.substring(0, next.length()-1);
            productList.add(next);
        }
        return productList;
    }

    private ReadOnlyKeyValueStore<String, Long> keyValueStore() {
        return this.interactiveQueryService.getQueryableStore(SaleStreamProcessing.PRODUCT_STATE_STORE,
                QueryableStoreTypes.keyValueStore());
    }
}
