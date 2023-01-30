package group09.BusinessIntelligenceService.Services;

import group09.BusinessIntelligenceService.Entities.SaleEvent;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class SaleStreamProcessing {

    public final static String PRODUCT_STATE_STORE = "product-state-store";

    @Bean
    public Function<KStream<?, SaleEvent>, KStream<String, Double>> process() {
        return inputStream -> {

            // Store products
            inputStream.map((key, value) -> {
                long productId = value.getProductId();
                int productQuantity = value.getQuantity();
                int price = value.getProductPrice();
                String productName = value.getProductName();

                SaleEvent saleEvent = new SaleEvent();
                saleEvent.setProductId(productId);
                saleEvent.setProductName(productName);
                saleEvent.setQuantity(productQuantity);
                saleEvent.setProductPrice(price);

                String new_key = productName+productId;
                return KeyValue.pair(new_key, saleEvent);
            }).toTable(
                    Materialized.<String, SaleEvent, KeyValueStore<Bytes, byte[]>>as(PRODUCT_STATE_STORE)
                            .withKeySerde(Serdes.String())
                            .withValueSerde(orderEventSerde())
            );
            // Logging Order Value
            KStream<String, Long> testStream = inputStream.map((key, value) -> {
                String productName = value.getProductName();
                long orderQuantity = value.getQuantity();
                return KeyValue.pair(productName, orderQuantity);
            });
            String ANSI_RESET = "\u001B[0m";
            String ANSI_CYAN = "\u001B[36m";
            testStream.print(Printed.<String, Long>toSysOut().withLabel(ANSI_CYAN + "New Order" + ANSI_RESET));

            // Map product and quantity into a KTable
            KTable<String, Double> orderQuantityKTable = inputStream.map((key, value) -> {
                String productName = value.getProductName();
                long orderQuantity = value.getQuantity();

                return
                        KeyValue.pair(productName, orderQuantity);
            }).groupByKey().aggregate(() -> 0.0,
                    (key, value, total) -> total + value,
                    Materialized.with(Serdes.String(), Serdes.Double()));

            // Log Quantity Summary
            KStream<String, Double> orderQuantityStream = orderQuantityKTable.toStream().map(KeyValue::pair);
            String ANSI_RED = "\u001B[31m";
            orderQuantityStream.print(Printed.<String, Double>toSysOut().withLabel(ANSI_RED + "Total Quantity" + ANSI_RESET));

            return orderQuantityStream;
        };
    }

    public Serde<SaleEvent> orderEventSerde() {
        final JsonSerde<SaleEvent> orderEventJsonSerde = new JsonSerde<>();
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "group09.BusinessIntelligenceService.Entities.SaleEvent");
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        orderEventJsonSerde.configure(configProps, false);
        return orderEventJsonSerde;
    }
}