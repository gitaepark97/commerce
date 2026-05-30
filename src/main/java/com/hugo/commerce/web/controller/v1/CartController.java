package com.hugo.commerce.web.controller.v1;

import com.hugo.commerce.domain.application.CartService;
import com.hugo.commerce.support.auth.User;
import com.hugo.commerce.support.response.ApiResponse;
import com.hugo.commerce.web.controller.v1.request.AddCartItemRequest;
import com.hugo.commerce.web.controller.v1.request.ModifyCartItemRequest;
import com.hugo.commerce.web.controller.v1.response.CartResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cart")
class CartController {

    private final CartService cartService;

    @GetMapping
    ApiResponse<CartResponse> getCart(User user) {
        return ApiResponse.success(CartResponse.from(cartService.getCart(user.id())));
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<Void> addCartItem(User user, @Valid @RequestBody AddCartItemRequest request) {
        cartService.addCartItem(user.id(), request.toCommand());
        return ApiResponse.success();
    }

    @PatchMapping("/items/{cartItemId}")
    ApiResponse<Void> modifyCartItem(User user, @PathVariable Long cartItemId, @Valid @RequestBody ModifyCartItemRequest request) {
        cartService.modifyCartItem(user.id(), request.toCommand(cartItemId));
        return ApiResponse.success();
    }

    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ApiResponse<Void> removeCartItem(User user, @PathVariable Long cartItemId) {
        cartService.removeCartItem(user.id(), cartItemId);
        return ApiResponse.success();
    }

}
