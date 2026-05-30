package com.hugo.commerce.domain.model;

import java.util.List;

public record Cart(
    Long userId,
    List<CartItemDetail> items
) {

}
