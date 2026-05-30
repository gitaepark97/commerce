package com.hugo.commerce.domain.application;

import com.hugo.commerce.domain.model.Product;
import com.hugo.commerce.domain.model.ProductDetail;
import com.hugo.commerce.support.Page;
import com.hugo.commerce.support.PageParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductFinder productFinder;

    public Page<Product> getProducts(Long categoryId, PageParam pageParam) {
        return productFinder.findProducts(categoryId, pageParam);
    }

    public ProductDetail getProduct(Long id) {
        return productFinder.findProduct(id);
    }

}
