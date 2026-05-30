package com.hugo.commerce.domain.application.command;

public record ModifyCartItem(
    Long cartItemId,
    int quantity
) {

}
