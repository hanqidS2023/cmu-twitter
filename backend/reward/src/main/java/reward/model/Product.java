package reward.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(unique = true)
    private String name;

    private int price;
    private String imageUrl;
    private boolean isPurchasable;

    protected Product() {
    }

    public Product(String name, int price, String imageUrl, boolean isPurchasable) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isPurchasable = isPurchasable;
    }
}
