package group09.ProcurementService.Entities;

import javax.persistence.*;

@Entity
@Table(name = "Contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String position;

    @ManyToOne
    @JoinColumn(name = "supplier_supplier_id")
    private Supplier supplier;

    public Contact() {
    }
    //id is auto-generated
    public Contact(String name, String phone, String email, String position, Supplier supplier) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.position = position;
        this.supplier = supplier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                ", supplier=" + supplier +
                '}';
    }
}
