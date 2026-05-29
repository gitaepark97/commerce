package com.hugo.commerce.web.controller.v1;

import com.hugo.commerce.domain.application.ProductService;
import com.hugo.commerce.support.Page;
import com.hugo.commerce.support.PageParam;
import com.hugo.commerce.support.response.ApiResponse;
import com.hugo.commerce.web.controller.v1.response.ProductDetailResponse;
import com.hugo.commerce.web.controller.v1.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
class ProductController {

    private final ProductService productService;

    @GetMapping
    ApiResponse<Page<ProductResponse>> getProducts(
        @RequestParam Long categoryId,
        @RequestParam(required = false) Long cursor,
        @RequestParam(required = false) Integer size
    ) {
        var pageParam = new PageParam(cursor, size);
        return ApiResponse.success(productService.getProducts(categoryId, pageParam).map(ProductResponse::from));
    }

    @GetMapping("/{productId}")
    ApiResponse<ProductDetailResponse> getProduct(@PathVariable Long productId) {
        var detail = productService.getProduct(productId);
        return ApiResponse.success(ProductDetailResponse.from(detail));
    }

}
