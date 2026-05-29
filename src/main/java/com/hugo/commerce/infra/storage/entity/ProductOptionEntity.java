package com.hugo.commerce.infra.storage.entity;

import com.hugo.commerce.domain.model.ProductOption;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "product_option")
public class ProductOptionEntity extends BaseEntity {

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Embedded
    private PriceEmbeddable price;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private int sortOrder;

    public ProductOption toDomain() {
        return new ProductOption(id, productId, name, description, price.toDomain(), stockQuantity);
    }

}
