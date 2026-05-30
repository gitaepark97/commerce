package com.hugo.commerce.domain.model;

public record CartItemDetail(
    Long id,
    ProductOptionDetail productOption,
    int quantity
) {

    public CartItemDetail(CartItem item, ProductOptionDetail productOption) {
        this(
            item.id(),
            productOption,
            item.quantity()
        );
    }

}
