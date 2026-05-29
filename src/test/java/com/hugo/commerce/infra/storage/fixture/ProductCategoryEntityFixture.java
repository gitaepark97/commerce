package com.hugo.commerce.infra.storage.fixture;

import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductCategoryEntity;

public class ProductCategoryEntityFixture {

    public static ProductCategoryEntity active(Long id, Long productId, Long categoryId) {
        return ProductCategoryEntity.builder()
            .id(id)
            .status(EntityStatus.ACTIVE)
            .productId(productId)
            .categoryId(categoryId)
            .build();
    }

    public static ProductCategoryEntity deleted(Long id, Long productId, Long categoryId) {
        return ProductCategoryEntity.builder()
            .id(id)
            .status(EntityStatus.DELETED)
            .productId(productId)
            .categoryId(categoryId)
            .build();
    }

}
