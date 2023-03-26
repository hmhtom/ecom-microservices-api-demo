package com.aheproject.product_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aheproject.product_service.dto.ProductRequest;
import com.aheproject.product_service.dto.ProductResponse;
import com.aheproject.product_service.model.Product;
import com.aheproject.product_service.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    // Inject ProductRepository
    private final ProductRepository productRepository;

    // ProductService - Create Product
    public void createProduct(ProductRequest productRequest) {

        // Creating a product object from the request
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        // Save the product object to the database
        productRepository.save(product);
        // Log the product id to console
        log.info("Product created with id: {}", product.getId());
    }

    // ProductService - Get All Product
    public List<ProductResponse> getAllProducts() {

        // Get all products from the database
        List<Product> products = productRepository.findAll();

        // Map and return the List<Product> to List<ProductResponse>
        return products.stream()
                .map(this::mapProductToProductResponse)
                .toList();
    }

    // Helper method to map product to product response
    private ProductResponse mapProductToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

}
