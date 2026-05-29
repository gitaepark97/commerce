package com.hugo.commerce.infra.storage;

import com.hugo.commerce.domain.model.ProductSection;
import com.hugo.commerce.domain.port.ProductSectionRepository;
import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductSectionEntity;
import com.hugo.commerce.infra.storage.repository.ProductSectionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductSectionRepositoryImpl implements ProductSectionRepository {

    private final ProductSectionJpaRepository productSectionJpaRepository;

    @Override
    public List<ProductSection> findByProductId(Long productId) {
        return productSectionJpaRepository.findByProductIdAndStatusOrderBySortOrderAsc(productId, EntityStatus.ACTIVE)
            .stream()
            .map(ProductSectionEntity::toDomain)
            .toList();
    }

}
