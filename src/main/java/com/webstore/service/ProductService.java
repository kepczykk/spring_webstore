package com.webstore.service;

import com.webstore.domain.Product;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(String productID);
    List<Product> getProductsByCategory(String category);
    Set<Product> getProductsByFilter(Map<String, List<String>> filterParams);
    List<Product> getProductsByManufacturer(String manufacturer);
    Set<Product> getProductsByPriceFilter(Map<String, List<String>> filterParams);
}
