package com.example.shoppingcartapp.model;

public class CartItem {
    private Product product;
    private int quantity;
    
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void incrementQuantity() {
        this.quantity++;
    }
    
    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }
    
    public double getItemTotal() {
        return product.getEffectivePrice() * quantity;
    }
    
    public double getTaxAmount() {
        return getItemTotal() * (product.getTaxGroup() / 100.0);
    }
}
