package group09.SalesService.Entities;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table
@PrimaryKeyJoinColumn(name = "saleId")
public class OnlineSale extends Sale {

    private String address;

    private String customerName;

    public OnlineSale() {
    }

    public OnlineSale(Long productId, int quantity, String dataAndTime, String address, String customerName){
        super(productId, quantity, dataAndTime);
        this.address = address;
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) { this.address = address; }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) { this.customerName = customerName; }

    @Override
    public String toString() {
        return "OnlineSale{" +
                "address='" + address + '\'' +
                ", customerName='" + customerName + '\'' +
                ", saleId=" + saleId +
                ", quantity=" + quantity +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", productId=" + productId +
                '}';
    }
}