package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 상품과 관련된 HTTP 요청 처리를 담당합니다.
 */
@RestController
@RequestMapping("/products")
@CrossOrigin
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final AuthenticationService authenticationService;

    /**
     * 모든 상품을 응답합니다.
     */
    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    /**
     * 주어진 id에 해당하는 상품을 찾아 응답합니다.
     *
     * @param id 찾고자 하는 상품의 식별자
     * @return 찾은 상품
     */
    @GetMapping("{id}")
    public Product findOne(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * 주어진 상품을 저장한 뒤, 저장된 상품을 응답합니다.
     *
     * @param authorization 권한 정보
     * @param productData 저장하고자 하는 상품 정보
     * @return 저장된 상품
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
            @RequestHeader("Authorization") String authorization,
            @RequestBody @Valid ProductData productData
    ) {
        String accessToken = authorization.substring("Bearer ".length());

        authenticationService.parseToken(accessToken);

        return productService.createProduct(productData);
    }

    /**
     * 주어진 id에 해당하는 상품을 찾아 수정하고, 수정된 상품을 응답합니다.
     *
     * @param authorization 권한 정보
     * @param id 수정하고자 하는 상품의 식별자
     * @param productData 수정하고자 하는 상품 정보
     * @return 수정된 상품
     */
    @PatchMapping("{id}")
    public Product update(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody @Valid ProductData productData
    ) {
        String accessToken = authorization.substring("Bearer ".length());

        authenticationService.parseToken(accessToken);

        return productService.updateProduct(id, productData);
    }

    /**
     * 주어진 id에 해당하는 상품을 찾아 삭제합니다.
     *
     * @param authorization 권한 정보
     * @param id 삭제하고자 하는 상품의 식별자
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id
    ) {
        String accessToken = authorization.substring("Bearer ".length());

        authenticationService.parseToken(accessToken);

        productService.deleteProduct(id);
    }

}
