package com.hugo.commerce.infra.storage.fixture;

import com.hugo.commerce.domain.enums.ProductStatus;
import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductEntity;

public class ProductEntityFixture {

    public static ProductEntity active(Long id) {
        return ProductEntity.builder()
            .id(id)
            .status(EntityStatus.ACTIVE)
            .name("상품 " + id)
            .thumbnailUrl("https://example.com/" + id + ".jpg")
            .description("상품 설명")
            .shortDescription("짧은 설명")
            .price(FixtureConstants.DEFAULT_PRICE)
            .productStatus(ProductStatus.ACTIVE)
            .build();
    }

    public static ProductEntity inactive(Long id) {
        return ProductEntity.builder()
            .id(id)
            .status(EntityStatus.ACTIVE)
            .name("상품 " + id)
            .thumbnailUrl("https://example.com/" + id + ".jpg")
            .price(FixtureConstants.DEFAULT_PRICE)
            .productStatus(ProductStatus.INACTIVE)
            .build();
    }

    public static ProductEntity deleted(Long id) {
        return ProductEntity.builder()
            .id(id)
            .status(EntityStatus.DELETED)
            .name("상품 " + id)
            .thumbnailUrl("https://example.com/" + id + ".jpg")
            .price(FixtureConstants.DEFAULT_PRICE)
            .productStatus(ProductStatus.INACTIVE)
            .build();
    }

}
