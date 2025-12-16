package com.example.shoppingcartapp.data;

import com.example.shoppingcartapp.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private static ProductRepository instance;
    private final List<Product> products;
    
    private ProductRepository() {
        products = new ArrayList<>(7);
        initializeProducts();
    }
    
    public static synchronized ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }
    
    private void initializeProducts() {
        products.add(new Product("1", "Wireless Headphones", 2500.0, 18, false));
        products.add(new Product("2", "Smart Watch", 15000.0, 18, true, 12000.0));
        products.add(new Product("3", "USB Cable", 299.0, 5, false));
        products.add(new Product("4", "Laptop Stand", 1500.0, 18, false));
        products.add(new Product("5", "Phone Case", 499.0, 5, true, 399.0));
        products.add(new Product("6", "Bluetooth Speaker", 3500.0, 18, false));
        products.add(new Product("7", "Mouse Pad", 199.0, 5, false));
    }
    
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
    
    public Product getProductById(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }
}
