package group09.InventoryService.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private Double price;
    private String comment;
    private int stockQuantity;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = {"product"})
    private List<Part> parts = new ArrayList<>();

    public Product() {
    }

    public Product(Long productId, String productName, Double price, String comment, int stockQuantity, List<Part> parts) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.comment = comment;
        this.stockQuantity = stockQuantity;
        this.parts = parts;
    }

    public Product(String productName, Double price, String comment, int stockQuantity, List<Part> parts) {
        this.productName = productName;
        this.price = price;
        this.comment = comment;
        this.stockQuantity = stockQuantity;
        this.parts = parts;
    }

    public Product(String productName, Double price, String comment, int stockQuantity) {
        this.productName = productName;
        this.price = price;
        this.comment = comment;
        this.stockQuantity = stockQuantity;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", comment='" + comment + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", parts=" + parts +
                '}';
    }
}
