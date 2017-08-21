package com.webstore.domain.repository;

import com.webstore.domain.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getAllProducts();
    Product getProductById(String productId);
}
