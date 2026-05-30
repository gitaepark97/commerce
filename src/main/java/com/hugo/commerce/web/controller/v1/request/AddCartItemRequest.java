package com.hugo.commerce.web.controller.v1.request;

import com.hugo.commerce.domain.application.command.AddCartItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequest(
    @NotNull Long productId,
    @NotNull Long productOptionId,
    @Min(1) int quantity
) {

    public AddCartItem toCommand() {
        return new AddCartItem(productId, productOptionId, quantity);
    }

}
