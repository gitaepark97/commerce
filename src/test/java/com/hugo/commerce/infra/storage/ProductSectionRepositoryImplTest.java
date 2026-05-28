package com.hugo.commerce.infra.storage;

import com.hugo.commerce.domain.model.ProductSection;
import com.hugo.commerce.domain.port.ProductSectionRepository;
import com.hugo.commerce.infra.storage.fixture.EntityFixture;
import com.hugo.commerce.infra.storage.repository.ProductJpaRepository;
import com.hugo.commerce.infra.storage.repository.ProductSectionJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class ProductSectionRepositoryImplTest {

    @Autowired
    private ProductSectionRepository productSectionRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ProductSectionJpaRepository productSectionJpaRepository;

    @Test
    @DisplayName("상품 ID로 ACTIVE 섹션을 sortOrder 오름차순으로 반환")
    void findByProductId_returnsSectionsInSortOrder() {
        // given
        productJpaRepository.save(EntityFixture.activeProduct(1L));
        productSectionJpaRepository.save(EntityFixture.activeSection(1L, 1L, 2));
        productSectionJpaRepository.save(EntityFixture.activeSection(2L, 1L, 1));
        productSectionJpaRepository.save(EntityFixture.activeSection(3L, 1L, 3));

        // when
        List<ProductSection> result = productSectionRepository.findByProductId(1L);

        // then
        assertThat(result).hasSize(3);
        assertThat(result).extracting(ProductSection::content)
            .containsExactly("섹션 내용 2", "섹션 내용 1", "섹션 내용 3");
    }

    @Test
    @DisplayName("DELETED 상태 섹션은 조회에서 제외")
    void findByProductId_excludesDeletedSections() {
        // given
        productJpaRepository.save(EntityFixture.activeProduct(1L));
        productSectionJpaRepository.save(EntityFixture.activeSection(1L, 1L, 1));
        productSectionJpaRepository.save(EntityFixture.deletedSection(2L, 1L, 2));

        // when
        List<ProductSection> result = productSectionRepository.findByProductId(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).content()).isEqualTo("섹션 내용 1");
    }

    @Test
    @DisplayName("섹션이 없는 상품 ID로 조회하면 빈 목록 반환")
    void findByProductId_returnsEmpty_whenNoSections() {
        // when
        List<ProductSection> result = productSectionRepository.findByProductId(999L);

        // then
        assertThat(result).isEmpty();
    }

}
