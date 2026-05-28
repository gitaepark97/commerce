package com.hugo.commerce.domain.port;

import java.util.List;

public interface ProductCategoryRepository {

    List<Long> findProductIdsByCategoryId(Long categoryId, Long cursor, int limit);

}
