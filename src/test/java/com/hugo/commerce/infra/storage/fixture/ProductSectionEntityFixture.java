package com.hugo.commerce.infra.storage.fixture;

import com.hugo.commerce.domain.enums.ProductSectionType;
import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductSectionEntity;

public class ProductSectionEntityFixture {

    public static ProductSectionEntity active(Long id, Long productId, int sortOrder) {
        return ProductSectionEntity.builder()
            .id(id)
            .status(EntityStatus.ACTIVE)
            .productId(productId)
            .type(ProductSectionType.IMAGE)
            .content("섹션 내용 " + id)
            .sortOrder(sortOrder)
            .build();
    }

    public static ProductSectionEntity deleted(Long id, Long productId, int sortOrder) {
        return ProductSectionEntity.builder()
            .id(id)
            .status(EntityStatus.DELETED)
            .productId(productId)
            .type(ProductSectionType.IMAGE)
            .content("섹션 내용 " + id)
            .sortOrder(sortOrder)
            .build();
    }

}
