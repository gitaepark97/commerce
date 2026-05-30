package com.hugo.commerce.domain.model;

import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;

public record ProductOption(
    Long id,
    Long productId,
    String name,
    String description,
    Price price,
    int stockQuantity
) {

    public record StockInfo(int availableQuantity) {}

    public boolean isSoldOut() {
        return stockQuantity == 0;
    }

    public void validateStock(int quantity) {
        if (stockQuantity < quantity) {
            throw new CoreException(ErrorType.INSUFFICIENT_STOCK, new StockInfo(stockQuantity));
        }
    }

}
