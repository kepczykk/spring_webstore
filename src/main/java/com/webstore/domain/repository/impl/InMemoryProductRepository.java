package com.webstore.domain.repository.impl;

import java.math.BigDecimal;
import java.util.*;

import com.webstore.exception.ProductNotFoundException;
import org.springframework.stereotype.Repository;
import com.webstore.domain.Product;
import com.webstore.domain.repository.ProductRepository;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    private List<Product> listOfProducts = new ArrayList<Product>();

    public InMemoryProductRepository() {
        Product iphone = new Product("P1234", "iPhone 5s", new BigDecimal(500));
        iphone.setDescription("Apple iPhone 5s, smartfon z 4-calowym ekranem o rozdzielczości 6401136 i 8-megapikselowym aparatem");
        iphone.setCategory("Smartfon");
        iphone.setManufacturer("Apple");
        iphone.setUnitsInStock(1000);
        Product laptop_dell = new Product("P1235", "Dell Inspiron", new BigDecimal(700));
        laptop_dell.setDescription("Dell Inspiron, 14-calowy laptop (czarny) z procesorem Intel Core 3. generacji");
        laptop_dell.setCategory("Laptop");
        laptop_dell.setManufacturer("Dell");
        laptop_dell.setUnitsInStock(1000);
        Product tablet_Nexus = new Product("P1236", "Nexus 7", new BigDecimal(300));
        tablet_Nexus.setDescription("Google Nexus 7 jest najlżejszym 7-calowym tabletem z 4-rdzeniowym procesorem Qualcomm Snapdragon™ S4 Pro");
        tablet_Nexus.setCategory("Tablet");
        tablet_Nexus.setManufacturer("Google");
        tablet_Nexus.setUnitsInStock(1000);
        listOfProducts.add(iphone);
        listOfProducts.add(laptop_dell);
        listOfProducts.add(tablet_Nexus);
    }

    public List<Product> getAllProducts() {
        return listOfProducts;
    }

    public Product getProductById(String productId) {
        Product productById = null;
        for (Product product : listOfProducts) {
            if (product != null && product.getProductId() != null && product.getProductId().equals(productId)) {
                productById = product;
                break;
            }
        }
        if (productById == null) {
            throw new ProductNotFoundException(productId);
        }
        return productById;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> productsByCategory = new ArrayList<Product>();
        for(Product product: listOfProducts) {
            if(category.equalsIgnoreCase(product.getCategory())){
                productsByCategory.add(product);
            }
        }
        return productsByCategory;
    }

    public Set<Product> getProductsByFilter(Map<String, List<String>> filterParams) {
        Set<Product> productsByBrand = new HashSet<Product>();
        Set<Product> productsByCategory = new HashSet<Product>();
        Set<String> criterias = filterParams.keySet();
        if(criterias.contains("brand")) {
            for(String brandName: filterParams.get("brand")) {
                for(Product product: listOfProducts) {
                    if(brandName.equalsIgnoreCase(product.getManufacturer())){
                        productsByBrand.add(product);
                    }
                }
            }
        }
        if(criterias.contains("category")) {
            for(String category: filterParams.get("category")) {
                productsByCategory.addAll(this.getProductsByCategory(category));
            }
        }
        productsByCategory.retainAll(productsByBrand);
        return productsByCategory;
    }

    public Set<Product> getProductsByPriceFilter(Map<String, List<String>> filterParams) {
        Set<Product> productsByPrice = new HashSet<Product>();
        Set<String> criterias = filterParams.keySet();
        if(criterias.contains("low") && criterias.contains("high")){
            for(String lowPrice: filterParams.get("low")) {
                for(String highPrice: filterParams.get("high")) {
                    for (Product product : listOfProducts) {
                        if (new BigDecimal(lowPrice).compareTo(product.getUnitPrice()) < 0
                                && new BigDecimal(highPrice).compareTo(product.getUnitPrice()) > 0) {
                            productsByPrice.add(product);
                        }
                    }
                }
            }
        }else {
            if (criterias.contains("low")) {
                for (String lowPrice : filterParams.get("low")) {
                    for (Product product : listOfProducts) {
                        if (new BigDecimal(lowPrice).compareTo(product.getUnitPrice()) < 0) {
                            productsByPrice.add(product);
                        }
                    }
                }
            } else {
                for (String highPrice : filterParams.get("high")) {
                    for (Product product : listOfProducts) {
                        if (new BigDecimal(highPrice).compareTo(product.getUnitPrice()) > 0) {
                            productsByPrice.add(product);
                        }
                    }
                }
            }
        }
        return productsByPrice;
    }

    public List<Product> getProductsByManufacturer(String manufacturer) {
        List<Product> productsByManufacturer = new ArrayList<Product>();
        for(Product product: listOfProducts) {
            if(manufacturer.equalsIgnoreCase(product.getManufacturer())){
                productsByManufacturer.add(product);
            }
        }
        return productsByManufacturer;
    }

    public void addProduct(Product product) {
        listOfProducts.add(product);
    }
}