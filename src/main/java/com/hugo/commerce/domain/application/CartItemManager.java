package com.hugo.commerce.domain.application;

import com.hugo.commerce.domain.application.command.AddCartItem;
import com.hugo.commerce.domain.model.CartItem;
import com.hugo.commerce.domain.port.CartItemRepository;
import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import com.hugo.commerce.support.provider.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Component
class CartItemManager {

    private final CartItemRepository cartItemRepository;
    private final IdGenerator idGenerator;

    void addCartItem(Long userId, AddCartItem command) {
        var existing = cartItemRepository.findByUserIdAndProductOptionId(userId, command.productOptionId());
        if (existing.isPresent()) {
            var item = existing.get();
            cartItemRepository.save(item.applyQuantity(item.quantity() + command.quantity()));
        } else {
            cartItemRepository.save(new CartItem(idGenerator.generate(), userId, command.productId(), command.productOptionId(), command.quantity()));
        }
    }

    void modifyCartItem(CartItem cartItem, int quantity) {
        cartItemRepository.save(cartItem.applyQuantity(quantity));
    }

    void removeCartItem(Long cartItemId, Long userId) {
        if (!cartItemRepository.deleteByIdAndUserId(cartItemId, userId)) {
            throw new CoreException(ErrorType.NOT_FOUND);
        }
    }

}
