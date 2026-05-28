package com.hugo.commerce.infra.storage;

import com.hugo.commerce.domain.port.ProductCategoryRepository;
import com.hugo.commerce.infra.storage.fixture.EntityFixture;
import com.hugo.commerce.infra.storage.repository.ProductCategoryJpaRepository;
import com.hugo.commerce.infra.storage.repository.ProductJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class ProductCategoryRepositoryImplTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ProductCategoryJpaRepository productCategoryJpaRepository;

    @Test
    @DisplayName("cursor 없이 limit만큼 상품 ID 반환")
    void findProductIdsByCategoryId_returnsLimitedIds_whenCursorIsNull() {
        // given
        Long categoryId = 1L;
        for (long i = 1; i <= 3; i++) {
            productJpaRepository.save(EntityFixture.activeProduct(i));
            productCategoryJpaRepository.save(EntityFixture.activeCategory(i, i, categoryId));
        }

        // when
        List<Long> result = productCategoryRepository.findProductIdsByCategoryId(categoryId, null, 2);

        // then
        assertThat(result).containsExactly(1L, 2L);

    }

    @Test
    @DisplayName("cursor 이후의 상품 ID 반환")
    void findProductIdsByCategoryId_returnsProductsAfterCursor() {
        // given
        Long categoryId = 1L;
        for (long i = 1; i <= 4; i++) {
            productJpaRepository.save(EntityFixture.activeProduct(i));
            productCategoryJpaRepository.save(EntityFixture.activeCategory(i * 100, i, categoryId));
        }

        // when
        List<Long> result = productCategoryRepository.findProductIdsByCategoryId(categoryId, 2L, 10);

        // then
        assertThat(result).containsExactly(3L, 4L);
    }

    @Test
    @DisplayName("DELETED 상태 카테고리 매핑은 조회에서 제외")
    void findProductIdsByCategoryId_excludesDeletedMappings() {
        // given
        Long categoryId = 1L;
        productJpaRepository.save(EntityFixture.activeProduct(1L));
        productJpaRepository.save(EntityFixture.activeProduct(2L));
        productCategoryJpaRepository.save(EntityFixture.activeCategory(1L, 1L, categoryId));
        productCategoryJpaRepository.save(EntityFixture.deletedCategory(2L, 2L, categoryId));

        // when
        List<Long> result = productCategoryRepository.findProductIdsByCategoryId(categoryId, null, 10);

        // then
        assertThat(result).containsExactly(1L);
    }

    @Test
    @DisplayName("카테고리에 상품이 없으면 빈 목록 반환")
    void findProductIdsByCategoryId_returnsEmpty_whenNoProducts() {
        // when
        List<Long> result = productCategoryRepository.findProductIdsByCategoryId(999L, null, 10);

        // then
        assertThat(result).isEmpty();
    }

}
