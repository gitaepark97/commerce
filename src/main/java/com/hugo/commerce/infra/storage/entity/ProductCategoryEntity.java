package com.hugo.commerce.infra.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "product_category")
public class ProductCategoryEntity extends BaseEntity {

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long categoryId;

}
