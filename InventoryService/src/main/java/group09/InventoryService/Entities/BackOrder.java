package group09.InventoryService.Entities;

import javax.persistence.*;

@Entity
@Table(name = "BackOrder")
public class BackOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backOrderSaleId;
    private Long supplierId;
    private Long partId;
    private int quantityNeeded;

    public BackOrder() {
    }

    public BackOrder(Long partId, Long supplierId, int quantityNeeded) {
        this.supplierId = supplierId;
        this.partId = partId;
        this.quantityNeeded = quantityNeeded;
    }

    public Long getBackOrderId() {
        return backOrderSaleId;
    }

    public void setBackOrderId(Long backOrderSaleId) {
        this.backOrderSaleId = backOrderSaleId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public int getQuantityNeeded() {
        return quantityNeeded;
    }

    public void setQuantityNeeded(int quantityNeeded) {
        this.quantityNeeded = quantityNeeded;
    }
}
