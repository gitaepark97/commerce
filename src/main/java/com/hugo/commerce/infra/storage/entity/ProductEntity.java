package com.hugo.commerce.infra.storage.entity;

import com.hugo.commerce.domain.enums.ProductStatus;
import com.hugo.commerce.domain.model.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "product")
public class ProductEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String thumbnailUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String shortDescription;

    @Embedded
    private PriceEmbeddable price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus productStatus;

    public Product toDomain() {
        return new Product(id, name, thumbnailUrl, description, shortDescription, price.toDomain(), productStatus);
    }

}
