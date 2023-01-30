package group09.InventoryService.Entities;
import javax.persistence.*;

@Entity
@Table
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partId;

    private String partName;

    private String description;

    private Long supplierId;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_product_id")
    private Product product;

    public Part() {
    }

    public Part(String partName, String description, Long supplierId, int quantity, Product product) {
        this.partId = partId;
        this.partName = partName;
        this.description = description;
        this.supplierId = supplierId;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Part{" +
                "partId=" + partId +
                ", partName='" + partName + '\'' +
                ", description='" + description + '\'' +
                ", supplierId=" + supplierId +
                ", quantity=" + quantity +
                ", product=" + product +
                '}';
    }
}

















