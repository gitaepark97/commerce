package com.hugo.commerce.infra.storage.entity;

import com.hugo.commerce.domain.model.Price;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PriceEmbeddable {

    private BigDecimal costPrice;
    private BigDecimal salesPrice;
    private BigDecimal discountedPrice;

    public Price toDomain() {
        return new Price(costPrice, salesPrice, discountedPrice);
    }

}
