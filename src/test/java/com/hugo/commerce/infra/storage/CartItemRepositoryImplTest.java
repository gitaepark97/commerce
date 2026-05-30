package com.hugo.commerce.infra.storage;

import com.hugo.commerce.domain.model.CartItem;
import com.hugo.commerce.domain.port.CartItemRepository;
import com.hugo.commerce.infra.storage.entity.BaseEntity;
import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.fixture.CartItemEntityFixture;
import com.hugo.commerce.infra.storage.repository.CartItemJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class CartItemRepositoryImplTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartItemJpaRepository cartItemJpaRepository;

    @Test
    @DisplayName("ID와 유저 ID로 ACTIVE 아이템 단건 조회")
    void findByIdAndUserId_returnsActiveItem() {
        // given
        cartItemJpaRepository.save(CartItemEntityFixture.active(1L, 10L, 200L));

        // when
        Optional<CartItem> result = cartItemRepository.findByIdAndUserId(1L, 10L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().userId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("다른 유저의 아이템은 조회되지 않음")
    void findByIdAndUserId_returnsEmpty_whenDifferentUser() {
        // given
        cartItemJpaRepository.save(CartItemEntityFixture.active(1L, 10L, 200L));

        // when
        Optional<CartItem> result = cartItemRepository.findByIdAndUserId(1L, 99L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("DELETED 아이템은 단건 조회에서 제외")
    void findByIdAndUserId_returnsEmpty_whenDeleted() {
        // given
        cartItemJpaRepository.save(CartItemEntityFixture.deleted(1L, 10L));

        // when
        Optional<CartItem> result = cartItemRepository.findByIdAndUserId(1L, 10L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("유저 ID로 ACTIVE 아이템 목록 반환")
    void findByUserId_returnsActiveItems() {
        // given
        cartItemJpaRepository.save(CartItemEntityFixture.active(1L, 10L, 200L));
        cartItemJpaRepository.save(CartItemEntityFixture.active(2L, 10L, 201L));
        cartItemJpaRepository.save(CartItemEntityFixture.active(3L, 99L, 200L));

        // when
        List<CartItem> result = cartItemRepository.findByUserId(10L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(CartItem::userId).containsOnly(10L);
    }

    @Test
    @DisplayName("DELETED 아이템은 목록 조회에서 제외")
    void findByUserId_excludesDeletedItems() {
        // given
        cartItemJpaRepository.save(CartItemEntityFixture.active(1L, 10L, 200L));
        cartItemJpaRepository.save(CartItemEntityFixture.deleted(2L, 10L));

        // when
        List<CartItem> result = cartItemRepository.findByUserId(10L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("유저 ID와 옵션 ID로 ACTIVE 아이템 단건 조회")
    void findByUserIdAndProductOptionId_returnsActiveItem() {
        // given
        cartItemJpaRepository.save(CartItemEntityFixture.active(1L, 10L, 200L));

        // when
        Optional<CartItem> result = cartItemRepository.findByUserIdAndProductOptionId(10L, 200L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().productOptionId()).isEqualTo(200L);
    }

    @Test
    @DisplayName("다른 유저의 옵션 아이템은 조회되지 않음")
    void findByUserIdAndProductOptionId_returnsEmpty_whenDifferentUser() {
        // given
        cartItemJpaRepository.save(CartItemEntityFixture.active(1L, 10L, 200L));

        // when
        Optional<CartItem> result = cartItemRepository.findByUserIdAndProductOptionId(99L, 200L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("장바구니 아이템 저장")
    void save_persistsCartItem() {
        // given
        var cartItem = new CartItem(1L, 10L, 100L, 200L, 3);

        // when
        CartItem result = cartItemRepository.save(cartItem);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(cartItemRepository.findByIdAndUserId(1L, 10L)).isPresent();
    }

    @Test
    @DisplayName("아이템 소유자가 삭제하면 DELETED 상태로 변경")
    void deleteByIdAndUserId_softDeletesItem() {
        // given
        cartItemJpaRepository.save(CartItemEntityFixture.active(1L, 10L, 200L));

        // when
        boolean result = cartItemRepository.deleteByIdAndUserId(1L, 10L);

        // then
        assertThat(result).isTrue();
        assertThat(cartItemRepository.findByIdAndUserId(1L, 10L)).isEmpty();
        assertThat(cartItemJpaRepository.findById(1L))
            .isPresent()
            .get()
            .extracting(BaseEntity::getStatus)
            .isEqualTo(EntityStatus.DELETED);
    }

    @Test
    @DisplayName("다른 유저의 아이템 삭제 시도 시 false 반환")
    void deleteByIdAndUserId_returnsFalse_whenDifferentUser() {
        // given
        cartItemJpaRepository.save(CartItemEntityFixture.active(1L, 10L, 200L));

        // when
        boolean result = cartItemRepository.deleteByIdAndUserId(1L, 99L);

        // then
        assertThat(result).isFalse();
        assertThat(cartItemRepository.findByIdAndUserId(1L, 10L)).isPresent();
    }

}
