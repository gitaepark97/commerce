package com.hugo.commerce.domain.model;

import com.hugo.commerce.domain.fixture.ProductOptionFixture;
import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductOptionTest {

    @Test
    @DisplayName("재고가 0이면 품절")
    void isSoldOut_whenStockIsZero() {
        // given
        var option = optionWithStock(0);

        // when & then
        assertThat(option.isSoldOut()).isTrue();
    }

    @Test
    @DisplayName("재고가 1 이상이면 품절 아님")
    void isNotSoldOut_whenStockIsPositive() {
        // given
        var option = optionWithStock(1);

        // when & then
        assertThat(option.isSoldOut()).isFalse();
    }

    @Test
    @DisplayName("재고가 충분하면 validateStock 통과")
    void validateStock_passes_whenStockIsSufficient() {
        // given
        var option = optionWithStock(5);

        // when & then (no exception)
        option.validateStock(5);
    }

    @Test
    @DisplayName("요청 수량이 재고를 초과하면 INSUFFICIENT_STOCK 예외 발생")
    void validateStock_throwsInsufficientStock_whenQuantityExceedsStock() {
        // given
        var option = optionWithStock(3);

        // when & then
        assertThatThrownBy(() -> option.validateStock(4))
            .isInstanceOf(CoreException.class)
            .satisfies(e -> {
                var ex = (CoreException) e;
                assertThat(ex.getErrorType()).isEqualTo(ErrorType.INSUFFICIENT_STOCK);
                assertThat(ex.getData()).isInstanceOf(ProductOption.StockInfo.class);
                assertThat(((ProductOption.StockInfo) ex.getData()).availableQuantity()).isEqualTo(3);
            });
    }

    @Test
    @DisplayName("재고가 0일 때 요청하면 INSUFFICIENT_STOCK 예외 발생")
    void validateStock_throwsInsufficientStock_whenSoldOut() {
        // given
        var option = optionWithStock(0);

        // when & then
        assertThatThrownBy(() -> option.validateStock(1))
            .isInstanceOf(CoreException.class)
            .satisfies(e -> assertThat(((CoreException) e).getErrorType()).isEqualTo(ErrorType.INSUFFICIENT_STOCK));
    }

    private ProductOption optionWithStock(int stock) {
        return new ProductOption(1L, 1L, "옵션", "설명",
            new Price(BigDecimal.valueOf(1000), BigDecimal.valueOf(1200), BigDecimal.valueOf(900)),
            stock);
    }

}
