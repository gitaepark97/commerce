package com.hugo.commerce.infra.storage.entity;

import com.hugo.commerce.domain.enums.ProductSectionType;
import com.hugo.commerce.domain.model.ProductSection;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "product_section")
public class ProductSectionEntity extends BaseEntity {

    @Column(nullable = false)
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductSectionType type;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    private int sortOrder;

    public ProductSection toDomain() {
        return new ProductSection(type, content);
    }

}
