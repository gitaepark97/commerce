package com.hugo.commerce.web.controller.v1.response;

import com.hugo.commerce.domain.model.Cart;
import com.hugo.commerce.domain.model.CartItemDetail;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
    List<CartItemResponse> items
) {

    public record CartItemResponse(
        Long id,
        int quantity,
        ProductInfo product,
        OptionInfo option
    ) {

        record ProductInfo(
            Long id,
            String name,
            String thumbnailUrl
        ) {}

        record OptionInfo(
            Long id,
            String name,
            BigDecimal salesPrice,
            BigDecimal discountedPrice,
            boolean isSoldOut
        ) {}

        static CartItemResponse from(CartItemDetail item) {
            var product = item.productOption().product();
            var option = item.productOption().option();
            return new CartItemResponse(
                item.id(),
                item.quantity(),
                new ProductInfo(product.id(), product.name(), product.thumbnailUrl()),
                new OptionInfo(
                    option.id(),
                    option.name(),
                    option.price().salesPrice(),
                    option.price().discountedPrice(),
                    option.isSoldOut()
                )
            );
        }

    }

    public static CartResponse from(Cart cart) {
        return new CartResponse(
            cart.items().stream().map(CartItemResponse::from).toList()
        );
    }

}
