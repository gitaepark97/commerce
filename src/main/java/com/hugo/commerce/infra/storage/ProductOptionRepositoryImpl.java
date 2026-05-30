package com.hugo.commerce.infra.storage;

import com.hugo.commerce.domain.model.ProductOption;
import com.hugo.commerce.domain.port.ProductOptionRepository;
import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductOptionEntity;
import com.hugo.commerce.infra.storage.repository.ProductOptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductOptionRepositoryImpl implements ProductOptionRepository {

    private final ProductOptionJpaRepository productOptionJpaRepository;

    @Override
    public Optional<ProductOption> findById(Long id) {
        return productOptionJpaRepository.findById(id)
            .filter(e -> e.getStatus() == EntityStatus.ACTIVE)
            .map(ProductOptionEntity::toDomain);
    }

    @Override
    public List<ProductOption> findByIds(Collection<Long> ids) {
        return productOptionJpaRepository.findAllByIdInAndStatus(ids, EntityStatus.ACTIVE)
            .stream()
            .map(ProductOptionEntity::toDomain)
            .toList();
    }

    @Override
    public List<ProductOption> findByProductId(Long productId) {
        return productOptionJpaRepository.findByProductIdAndStatusOrderBySortOrderAsc(productId, EntityStatus.ACTIVE)
            .stream()
            .map(ProductOptionEntity::toDomain)
            .toList();
    }

}
