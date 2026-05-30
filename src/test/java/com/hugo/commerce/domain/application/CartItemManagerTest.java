package com.hugo.commerce.domain.application;

import com.hugo.commerce.domain.application.command.AddCartItem;
import com.hugo.commerce.domain.fake.FakeCartItemRepository;
import com.hugo.commerce.domain.model.CartItem;
import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartItemManagerTest {

    private CartItemManager cartItemManager;
    private FakeCartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        cartItemRepository = new FakeCartItemRepository();
        AtomicLong idSequence = new AtomicLong(1);
        cartItemManager = new CartItemManager(cartItemRepository, idSequence::getAndIncrement);
    }

    @Test
    @DisplayName("장바구니 아이템 추가")
    void addsCartItem() {
        // given
        var command = new AddCartItem(100L, 200L, 3);

        // when
        cartItemManager.addCartItem(10L, command);

        // then
        var items = cartItemRepository.findByUserId(10L);
        assertThat(items).hasSize(1);
        assertThat(items.get(0).productOptionId()).isEqualTo(200L);
        assertThat(items.get(0).quantity()).isEqualTo(3);
    }

    @Test
    @DisplayName("같은 옵션이 이미 있으면 수량을 합산")
    void mergesQuantity_whenSameOptionAlreadyExists() {
        // given
        cartItemRepository.save(new CartItem(1L, 10L, 100L, 200L, 3));
        var command = new AddCartItem(100L, 200L, 2);

        // when
        cartItemManager.addCartItem(10L, command);

        // then
        var items = cartItemRepository.findByUserId(10L);
        assertThat(items).hasSize(1);
        assertThat(items.get(0).quantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("장바구니 아이템 수량 수정")
    void modifiesCartItemQuantity() {
        // given
        var cartItem = new CartItem(1L, 10L, 100L, 200L, 3);
        cartItemRepository.save(cartItem);

        // when
        cartItemManager.modifyCartItem(cartItem, 5);

        // then
        var updated = cartItemRepository.findByUserId(10L).getFirst();
        assertThat(updated.quantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("장바구니 아이템 삭제")
    void removesCartItem() {
        // given
        cartItemRepository.save(new CartItem(1L, 10L, 100L, 200L, 3));

        // when
        cartItemManager.removeCartItem(1L, 10L);

        // then
        assertThat(cartItemRepository.findByUserId(10L)).isEmpty();
    }

    @Test
    @DisplayName("다른 유저의 아이템 삭제 시 NOT_FOUND 예외 발생")
    void throwsNotFound_whenRemovingAnotherUsersItem() {
        // given
        cartItemRepository.save(new CartItem(1L, 10L, 100L, 200L, 3));

        // when & then
        assertThatThrownBy(() -> cartItemManager.removeCartItem(1L, 99L))
            .isInstanceOf(CoreException.class)
            .satisfies(e -> assertThat(((CoreException) e).getErrorType()).isEqualTo(ErrorType.NOT_FOUND));
    }

}
