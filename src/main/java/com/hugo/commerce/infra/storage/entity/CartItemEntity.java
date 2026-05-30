package com.hugo.commerce.infra.storage.entity;

import com.hugo.commerce.domain.model.CartItem;
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
@Table(name = "cart_item")
public class CartItemEntity extends BaseEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long productOptionId;

    @Column(nullable = false)
    private int quantity;

    public static CartItemEntity fromDomain(CartItem cartItem) {
        return CartItemEntity.builder()
            .id(cartItem.id())
            .status(EntityStatus.ACTIVE)
            .userId(cartItem.userId())
            .productId(cartItem.productId())
            .productOptionId(cartItem.productOptionId())
            .quantity(cartItem.quantity())
            .build();
    }

    public CartItem toDomain() {
        return new CartItem(id, userId, productId, productOptionId, quantity);
    }

}
