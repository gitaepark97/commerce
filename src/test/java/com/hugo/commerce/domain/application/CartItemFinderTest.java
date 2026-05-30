package com.hugo.commerce.domain.application;

import com.hugo.commerce.domain.fake.FakeCartItemRepository;
import com.hugo.commerce.domain.model.CartItem;
import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartItemFinderTest {

    private CartItemFinder cartItemFinder;
    private FakeCartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        cartItemRepository = new FakeCartItemRepository();
        cartItemFinder = new CartItemFinder(cartItemRepository);
    }

    @Test
    @DisplayName("ID와 유저 ID로 장바구니 아이템 단건 반환")
    void returnsCartItem_whenExists() {
        // given
        cartItemRepository.save(new CartItem(1L, 10L, 100L, 200L, 3));

        // when
        CartItem result = cartItemFinder.findCartItem(1L, 10L);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.userId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("다른 유저의 아이템 조회 시 NOT_FOUND 예외 발생")
    void throwsNotFound_whenCartItemBelongsToAnotherUser() {
        // given
        cartItemRepository.save(new CartItem(1L, 10L, 100L, 200L, 3));

        // when & then
        assertThatThrownBy(() -> cartItemFinder.findCartItem(1L, 99L))
            .isInstanceOf(CoreException.class)
            .satisfies(e -> assertThat(((CoreException) e).getErrorType()).isEqualTo(ErrorType.NOT_FOUND));
    }

    @Test
    @DisplayName("존재하지 않는 아이템 ID로 조회 시 NOT_FOUND 예외 발생")
    void throwsNotFound_whenCartItemDoesNotExist() {
        // when & then
        assertThatThrownBy(() -> cartItemFinder.findCartItem(999L, 10L))
            .isInstanceOf(CoreException.class)
            .satisfies(e -> assertThat(((CoreException) e).getErrorType()).isEqualTo(ErrorType.NOT_FOUND));
    }

    @Test
    @DisplayName("유저 ID로 장바구니 아이템 목록 반환")
    void returnsCartItems_whenUserHasItems() {
        // given
        cartItemRepository.save(new CartItem(1L, 10L, 100L, 200L, 1));
        cartItemRepository.save(new CartItem(2L, 10L, 101L, 201L, 2));
        cartItemRepository.save(new CartItem(3L, 99L, 100L, 200L, 1));

        // when
        List<CartItem> result = cartItemFinder.findCartItems(10L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(CartItem::userId).containsOnly(10L);
    }

    @Test
    @DisplayName("장바구니가 비어있으면 빈 목록 반환")
    void returnsEmptyList_whenCartIsEmpty() {
        // when
        List<CartItem> result = cartItemFinder.findCartItems(10L);

        // then
        assertThat(result).isEmpty();
    }


}
