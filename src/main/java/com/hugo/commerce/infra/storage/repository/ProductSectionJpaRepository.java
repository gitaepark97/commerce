package com.hugo.commerce.infra.storage.repository;

import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductSectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSectionJpaRepository extends JpaRepository<ProductSectionEntity, Long> {

    List<ProductSectionEntity> findByProductIdAndStatusOrderBySortOrderAsc(Long productId, EntityStatus status);

}
