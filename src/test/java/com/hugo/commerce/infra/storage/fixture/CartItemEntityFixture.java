package com.hugo.commerce.infra.storage.fixture;

import com.hugo.commerce.infra.storage.entity.CartItemEntity;
import com.hugo.commerce.infra.storage.entity.EntityStatus;

public class CartItemEntityFixture {

    public static CartItemEntity active(Long id, Long userId, Long productOptionId) {
        return CartItemEntity.builder()
            .id(id)
            .status(EntityStatus.ACTIVE)
            .userId(userId)
            .productId(1L)
            .productOptionId(productOptionId)
            .quantity(1)
            .build();
    }

    public static CartItemEntity deleted(Long id, Long userId) {
        return CartItemEntity.builder()
            .id(id)
            .status(EntityStatus.DELETED)
            .userId(userId)
            .productId(1L)
            .productOptionId(1L)
            .quantity(1)
            .build();
    }

}
