package com.hugo.commerce.infra.storage.fixture;

import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductOptionEntity;

public class ProductOptionEntityFixture {

    public static ProductOptionEntity active(Long id, Long productId, int sortOrder) {
        return ProductOptionEntity.builder()
            .id(id)
            .status(EntityStatus.ACTIVE)
            .productId(productId)
            .name("옵션 " + id)
            .description("옵션 설명")
            .price(FixtureConstants.DEFAULT_PRICE)
            .stockQuantity(100)
            .sortOrder(sortOrder)
            .build();
    }

    public static ProductOptionEntity deleted(Long id, Long productId, int sortOrder) {
        return ProductOptionEntity.builder()
            .id(id)
            .status(EntityStatus.DELETED)
            .productId(productId)
            .name("옵션 " + id)
            .price(FixtureConstants.DEFAULT_PRICE)
            .stockQuantity(0)
            .sortOrder(sortOrder)
            .build();
    }

}
