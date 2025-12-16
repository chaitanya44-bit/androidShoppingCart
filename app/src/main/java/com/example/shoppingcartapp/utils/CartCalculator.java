package com.example.shoppingcartapp.utils;

import com.example.shoppingcartapp.model.CartItem;
import java.util.List;

public class CartCalculator {
    private static final double MIN_CART_VALUE = 1000.0;
    private static final double COUPON_DISCOUNT_PERCENT = 20.0;
    private static final double MAX_COUPON_DISCOUNT = 7300.0;
    private static final double PERCENT_DIVISOR = 100.0;
    
    public static double calculateItemTotal(List<CartItem> cartItems) {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getItemTotal();
        }
        return total;
    }
    
    public static double calculateTaxTotal(List<CartItem> cartItems) {
        double taxTotal = 0.0;
        for (CartItem item : cartItems) {
            taxTotal += item.getTaxAmount();
        }
        return taxTotal;
    }
    
    public static double calculateSubtotal(List<CartItem> cartItems) {
        return calculateItemTotal(cartItems) + calculateTaxTotal(cartItems);
    }
    
    public static double calculateEligibleCartValue(List<CartItem> cartItems) {
        double eligibleTotal = 0.0;
        for (CartItem item : cartItems) {
            if (!item.getProduct().isDiscounted()) {
                eligibleTotal += item.getItemTotal();
            }
        }
        return eligibleTotal;
    }
    
    public static boolean isCouponApplicable(List<CartItem> cartItems) {
        return calculateEligibleCartValue(cartItems) >= MIN_CART_VALUE;
    }
    
    public static double calculateCouponDiscount(List<CartItem> cartItems) {
        if (!isCouponApplicable(cartItems)) {
            return 0.0;
        }
        double eligibleValue = calculateEligibleCartValue(cartItems);
        double discount = eligibleValue * (COUPON_DISCOUNT_PERCENT / PERCENT_DIVISOR);
        return discount > MAX_COUPON_DISCOUNT ? MAX_COUPON_DISCOUNT : discount;
    }
    
    public static double calculateFinalAmount(List<CartItem> cartItems, double couponDiscount) {
        return calculateSubtotal(cartItems) - couponDiscount;
    }
    
    public static String getCouponErrorMessage(List<CartItem> cartItems) {
        double eligibleValue = calculateEligibleCartValue(cartItems);
        if (eligibleValue < MIN_CART_VALUE) {
            double remaining = MIN_CART_VALUE - eligibleValue;
            return String.format("Add ₹%.2f more (non-discounted items) to apply coupon", remaining);
        }
        return "Coupon not applicable";
    }
}
