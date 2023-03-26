package com.aheproject.product_service.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aheproject.product_service.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
