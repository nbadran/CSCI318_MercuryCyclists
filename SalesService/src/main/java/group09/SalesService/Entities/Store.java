package group09.SalesService.Entities;

import java.util.ArrayList; // import the ArrayList class
import java.util.List;
import javax.persistence.*;
import javax.transaction.Transactional;

@Entity
@Table
@Transactional
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;
    private String address;
    private String manager;
    private String name;

    @OneToMany
    //@JsonIgnoreProperties(value = {"store"})
    @JoinColumn(name = "sc_foreignKey", referencedColumnName = "storeId")
    private List<InStoreSale> sales = new ArrayList<>();

    public Store(){} //copy constructor

    public Store(Long storeId, String address, String manager, String name, List<InStoreSale> sales) {
        this.storeId = storeId;
        this.address = address;
        this.manager = manager;
        this.name = name;
        this.sales = sales;
    }

    public Store(String address, String manager, String name, List<InStoreSale> sales) {
        this.address = address;
        this.manager = manager;
        this.name = name;
        this.sales = sales;
    }

    public Store(String address, String manager, String name) {
        this.address = address;
        this.manager = manager;
        this.name = name;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) { this.address = address;}

    public String getManager() { return manager; }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InStoreSale> getSales() { return this.sales; }

    public void setSales(List<InStoreSale> sales) { this.sales = sales; }

    public void addSale(InStoreSale iss) { sales.add(iss); }

    @Override
    public String toString() {
        return "Store{" +
                "storeId=" + storeId +
                ", address='" + address + '\'' +
                ", manager='" + manager + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
