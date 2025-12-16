package com.example.shoppingcartapp.model;

public class Product {
    private String id;
    private String name;
    private double originalPrice;
    private int taxGroup;
    private boolean isDiscounted;
    private double discountedPrice;
    
    public Product(String id, String name, double originalPrice, int taxGroup, boolean isDiscounted) {
        this.id = id;
        this.name = name;
        this.originalPrice = originalPrice;
        this.taxGroup = taxGroup;
        this.isDiscounted = isDiscounted;
        this.discountedPrice = isDiscounted ? originalPrice * 0.8 : originalPrice;
    }
    
    public Product(String id, String name, double originalPrice, int taxGroup, boolean isDiscounted, double discountedPrice) {
        this.id = id;
        this.name = name;
        this.originalPrice = originalPrice;
        this.taxGroup = taxGroup;
        this.isDiscounted = isDiscounted;
        this.discountedPrice = discountedPrice;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public double getOriginalPrice() {
        return originalPrice;
    }
    
    public int getTaxGroup() {
        return taxGroup;
    }
    
    public boolean isDiscounted() {
        return isDiscounted;
    }
    
    public double getDiscountedPrice() {
        return discountedPrice;
    }
    
    public double getEffectivePrice() {
        return isDiscounted ? discountedPrice : originalPrice;
    }
}
