package com.hugo.commerce.infra.storage.repository;

import com.hugo.commerce.infra.storage.entity.EntityStatus;
import com.hugo.commerce.infra.storage.entity.ProductOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionJpaRepository extends JpaRepository<ProductOptionEntity, Long> {

    List<ProductOptionEntity> findByProductIdAndStatus(Long productId, EntityStatus status);

}
