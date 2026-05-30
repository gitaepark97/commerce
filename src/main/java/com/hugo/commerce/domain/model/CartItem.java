package com.hugo.commerce.domain.model;

public record CartItem(
    Long id,
    Long userId,
    Long productId,
    Long productOptionId,
    int quantity
) {

    public CartItem applyQuantity(Integer quantity) {
        return new CartItem(id, userId, productId, productOptionId, quantity);
    }

}
