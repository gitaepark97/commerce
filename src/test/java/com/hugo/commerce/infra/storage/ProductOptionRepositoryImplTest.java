package com.hugo.commerce.infra.storage;

import com.hugo.commerce.domain.model.ProductOption;
import com.hugo.commerce.domain.port.ProductOptionRepository;
import com.hugo.commerce.infra.storage.fixture.ProductEntityFixture;
import com.hugo.commerce.infra.storage.fixture.ProductOptionEntityFixture;
import com.hugo.commerce.infra.storage.repository.ProductJpaRepository;
import com.hugo.commerce.infra.storage.repository.ProductOptionJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class ProductOptionRepositoryImplTest {

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ProductOptionJpaRepository productOptionJpaRepository;

    @Test
    @DisplayName("상품 ID로 ACTIVE 옵션 목록 반환")
    void findByProductId_returnsActiveOptions() {
        // given
        productJpaRepository.save(ProductEntityFixture.active(1L));
        productOptionJpaRepository.save(ProductOptionEntityFixture.active(1L, 1L, 1));
        productOptionJpaRepository.save(ProductOptionEntityFixture.active(2L, 1L, 2));

        // when
        List<ProductOption> result = productOptionRepository.findByProductId(1L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(ProductOption::id).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    @DisplayName("상품 ID로 ACTIVE 옵션을 sortOrder 오름차순으로 반환")
    void findByProductId_returnsOptionsInSortOrder() {
        // given
        productJpaRepository.save(ProductEntityFixture.active(1L));
        productOptionJpaRepository.save(ProductOptionEntityFixture.active(1L, 1L, 2));
        productOptionJpaRepository.save(ProductOptionEntityFixture.active(2L, 1L, 1));
        productOptionJpaRepository.save(ProductOptionEntityFixture.active(3L, 1L, 3));

        // when
        List<ProductOption> result = productOptionRepository.findByProductId(1L);

        // then
        assertThat(result).hasSize(3);
        assertThat(result).extracting(ProductOption::name).containsExactly("옵션 2", "옵션 1", "옵션 3");
    }

    @Test
    @DisplayName("DELETED 상태 옵션은 조회에서 제외")
    void findByProductId_excludesDeletedOptions() {
        // given
        productJpaRepository.save(ProductEntityFixture.active(1L));
        productOptionJpaRepository.save(ProductOptionEntityFixture.active(1L, 1L, 1));
        productOptionJpaRepository.save(ProductOptionEntityFixture.deleted(2L, 1L, 2));

        // when
        List<ProductOption> result = productOptionRepository.findByProductId(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("옵션이 없는 상품 ID로 조회하면 빈 목록 반환")
    void findByProductId_returnsEmpty_whenNoOptions() {
        // when
        List<ProductOption> result = productOptionRepository.findByProductId(999L);

        // then
        assertThat(result).isEmpty();
    }

}
