package group09.SalesService.Entities;

import javax.persistence.*;

@Entity
@Table
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long saleId;

    protected int quantity;

    protected String dateAndTime;

    protected Long productId;

    public Sale() {
    }

    public Sale(Long saleId, Long productId, int quantity, String dateAndTime){
        this.saleId = saleId;
        this.quantity = quantity;
        this.dateAndTime = dateAndTime;
        this.productId = productId;
    }

    public Sale(Long productId, int quantity, String dateAndTime){
        this.quantity = quantity;
        this.dateAndTime = dateAndTime;
        this.productId = productId;
    }
    public Long getSaleId() {
        return this.saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleId=" + saleId +
                ", quantity=" + quantity +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", productId=" + productId +
                '}';
    }
}

