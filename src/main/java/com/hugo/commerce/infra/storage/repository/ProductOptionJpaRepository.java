package com.hugo.commerce.infra.storage.repository;

import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductOptionJpaRepository extends JpaRepository<ProductOptionEntity, Long> {

    List<ProductOptionEntity> findByProductIdAndStatusOrderBySortOrderAsc(Long productId, EntityStatus status);

    List<ProductOptionEntity> findAllByIdInAndStatus(Collection<Long> ids, EntityStatus status);

}
