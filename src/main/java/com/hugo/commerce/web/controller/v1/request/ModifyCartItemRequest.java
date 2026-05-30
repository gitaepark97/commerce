package com.hugo.commerce.web.controller.v1.request;

import com.hugo.commerce.domain.application.command.ModifyCartItem;
import jakarta.validation.constraints.Min;

public record ModifyCartItemRequest(
    @Min(1) int quantity
) {

    public ModifyCartItem toCommand(Long cartItemId) {
        return new ModifyCartItem(cartItemId, quantity);
    }

}
