package group09.ProcurementService.Entities;

import javax.persistence.*;

@Entity
@Table(name = "BackOrderSale")
public class BackOrderSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backOrderSaleId;
    private Long supplierId;
    private Long partId;
    private int quantityNeeded;

    public BackOrderSale() {
    }

    public BackOrderSale(Long supplierId, Long partId, int quantityNeeded) {
        this.supplierId = supplierId;
        this.partId = partId;
        this.quantityNeeded = quantityNeeded;
    }

//    public Long getBackOrderSaleId() {
//        return backOrderSaleId;
//    }
//
//    public void setBackOrderSaleId(Long backOrderSaleId) {
//        this.backOrderSaleId = backOrderSaleId;
//    }

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

    @Override
    public String toString() {
        return "BackOrderSale{" +
                "supplierId=" + supplierId +
                ", productId=" + partId +
                ", quantityNeeded=" + quantityNeeded +
                '}';
    }
}
