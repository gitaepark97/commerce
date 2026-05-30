package com.hugo.commerce.infra.storage;

import com.hugo.commerce.domain.model.CartItem;
import com.hugo.commerce.domain.port.CartItemRepository;
import com.hugo.commerce.infra.storage.entity.CartItemEntity;
import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.repository.CartItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepository {

    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public Optional<CartItem> findByIdAndUserId(Long id, Long userId) {
        return cartItemJpaRepository.findByIdAndUserIdAndStatus(id, userId, EntityStatus.ACTIVE)
            .map(CartItemEntity::toDomain);
    }

    @Override
    public Optional<CartItem> findByUserIdAndProductOptionId(Long userId, Long productOptionId) {
        return cartItemJpaRepository.findByUserIdAndProductOptionIdAndStatus(userId, productOptionId, EntityStatus.ACTIVE)
            .map(CartItemEntity::toDomain);
    }

    @Override
    public List<CartItem> findByUserId(Long userId) {
        return cartItemJpaRepository.findByUserIdAndStatus(userId, EntityStatus.ACTIVE)
            .stream()
            .map(CartItemEntity::toDomain)
            .toList();
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemJpaRepository.save(CartItemEntity.fromDomain(cartItem)).toDomain();
    }

    @Override
    public boolean deleteByIdAndUserId(Long id, Long userId) {
        return cartItemJpaRepository.updateStatusByIdAndUserId(id, userId, EntityStatus.DELETED) > 0;
    }

}
