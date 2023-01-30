package group09.SalesService.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table
@PrimaryKeyJoinColumn(name = "saleId")
public class InStoreSale extends Sale {
    private int receiptNo;
    //private Long storeId;
    @ManyToOne
    @JoinColumn(name = "store_id")
    @JsonIgnoreProperties(value={"address", "manager", "name", "inStoreSales", "hibernateLazyInitializer", "handler"})
    private Store store;

    public InStoreSale() {
    }

    InStoreSale(Long saleId, Long productId, int quantity, String dataAndTime, int receiptNo, Store store){ //, Long storeId){
        super(saleId, productId, quantity, dataAndTime);
        this.receiptNo = receiptNo;
        //this.storeId = storeId;
        this.store = store;
    }

    public InStoreSale(Long productId, int quantity, String dataAndTime, int receiptNo, Store store){ //, Long storeId){
        super(productId, quantity, dataAndTime);
        this.receiptNo = receiptNo;
        //this.storeId = storeId;
        this.store = store;
    }

    public void setReceiptNo(int receiptNo){
        this.receiptNo = receiptNo;
    }

    public int getReceiptNo(){ return receiptNo; }

//    public Long getStoreId() {
//        return storeId;
//    }
//
//    public void setStoreId(Long storeId) {
//        this.storeId = storeId;
//    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "InStoreSale{" +
                "saleId=" + saleId +
                ", receiptNo=" + receiptNo +
                ", quantity=" + quantity +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", productId=" + productId +
                '}';
    }
}
