package com.hugo.commerce.infra.storage;

import com.hugo.commerce.domain.model.ProductOption;
import com.hugo.commerce.domain.port.ProductOptionRepository;
import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductOptionEntity;
import com.hugo.commerce.infra.storage.repository.ProductOptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductOptionRepositoryImpl implements ProductOptionRepository {

    private final ProductOptionJpaRepository productOptionJpaRepository;

    @Override
    public List<ProductOption> findByProductId(Long productId) {
        return productOptionJpaRepository.findByProductIdAndStatus(productId, EntityStatus.ACTIVE)
            .stream()
            .map(ProductOptionEntity::toDomain)
            .toList();
    }

}
