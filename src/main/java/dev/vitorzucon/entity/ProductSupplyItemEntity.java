package dev.vitorzucon.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_productsupplyitem")
public class ProductSupplyItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "supply_id", nullable = false)
    private SupplyEntity supply;

    @Column(nullable = false)
    private Double quantity;

    public SupplyEntity getSupply() {
        return supply;
    }

    public void setSupply(SupplyEntity supply) {
        this.supply = supply;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }
}
