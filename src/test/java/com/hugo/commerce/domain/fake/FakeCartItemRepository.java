package com.hugo.commerce.domain.fake;

import com.hugo.commerce.domain.model.CartItem;
import com.hugo.commerce.domain.port.CartItemRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeCartItemRepository implements CartItemRepository {

    private final Map<Long, CartItem> store = new HashMap<>();

    @Override
    public Optional<CartItem> findByIdAndUserId(Long id, Long userId) {
        return Optional.ofNullable(store.get(id))
            .filter(item -> item.userId().equals(userId));
    }

    @Override
    public Optional<CartItem> findByUserIdAndProductOptionId(Long userId, Long productOptionId) {
        return store.values().stream()
            .filter(item -> item.userId().equals(userId) && item.productOptionId().equals(productOptionId))
            .findFirst();
    }

    @Override
    public List<CartItem> findByUserId(Long userId) {
        return store.values().stream()
            .filter(item -> item.userId().equals(userId))
            .toList();
    }

    @Override
    public CartItem save(CartItem cartItem) {
        store.put(cartItem.id(), cartItem);
        return cartItem;
    }

    @Override
    public boolean deleteByIdAndUserId(Long id, Long userId) {
        var item = store.get(id);
        if (item == null || !item.userId().equals(userId)) return false;
        store.remove(id);
        return true;
    }

}
