package com.hugo.commerce.domain.application;

import com.hugo.commerce.domain.model.CartItem;
import com.hugo.commerce.domain.port.CartItemRepository;
import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
class CartItemFinder {

    private final CartItemRepository cartItemRepository;

    CartItem findCartItem(Long cartItemId, Long userId) {
        return cartItemRepository.findByIdAndUserId(cartItemId, userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND));
    }

    List<CartItem> findCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

}
