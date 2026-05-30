package com.hugo.commerce.domain.application.command;

public record AddCartItem(
    Long productId,
    Long productOptionId,
    int quantity
) {

}
