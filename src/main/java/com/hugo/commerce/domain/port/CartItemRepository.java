package com.hugo.commerce.domain.port;

import com.hugo.commerce.domain.model.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository {

    Optional<CartItem> findByIdAndUserId(Long id, Long userId);

    Optional<CartItem> findByUserIdAndProductOptionId(Long userId, Long productOptionId);

    List<CartItem> findByUserId(Long userId);

    CartItem save(CartItem cartItem);

    boolean deleteByIdAndUserId(Long id, Long userId);

}
