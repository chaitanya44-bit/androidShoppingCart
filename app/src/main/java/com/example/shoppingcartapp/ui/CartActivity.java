package com.example.shoppingcartapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingcartapp.R;
import com.example.shoppingcartapp.adapter.CartAdapter;
import com.example.shoppingcartapp.model.CartItem;
import com.example.shoppingcartapp.utils.CartCalculator;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView tvItemTotal;
    private TextView tvTaxTotal;
    private TextView tvSubtotal;
    private TextView tvCouponDiscount;
    private TextView tvFinalAmount;
    private Button btnApplyCoupon;
    private Button btnCheckout;
    private View emptyCartView;
    private View cartSummaryView;
    private List<CartItem> cartItems;
    private double couponDiscount = 0.0;
    private boolean couponApplied = false;
    private static final String PRICE_FORMAT = "₹%.2f";
    private static final String COUPON_APPLIED = "Coupon Applied";
    private static final String APPLY_COUPON = "Apply Coupon";
    private static final String COUPON_SUCCESS = "Coupon applied successfully! Discount: ₹%.2f";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        initializeViews();
        loadCartItems();
        setupRecyclerView();
        setupButtons();
        updateCartSummary();
    }
    
    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewCart);
        tvItemTotal = findViewById(R.id.tvItemTotal);
        tvTaxTotal = findViewById(R.id.tvTaxTotal);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvCouponDiscount = findViewById(R.id.tvCouponDiscount);
        tvFinalAmount = findViewById(R.id.tvFinalAmount);
        btnApplyCoupon = findViewById(R.id.btnApplyCoupon);
        btnCheckout = findViewById(R.id.btnCheckout);
        emptyCartView = findViewById(R.id.emptyCartView);
        cartSummaryView = findViewById(R.id.cartSummaryView);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void loadCartItems() {
        cartItems = MainActivity.getCartItems();
    }
    
    private void setupRecyclerView() {
        adapter = new CartAdapter(cartItems, new CartAdapter.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged() {
                updateCartSummary();
            }
            
            @Override
            public void onItemRemoved(int position) {
                cartItems.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, cartItems.size());
                updateCartSummary();
                
                if (cartItems.isEmpty() || !CartCalculator.isCouponApplicable(cartItems)) {
                    couponApplied = false;
                    couponDiscount = 0.0;
                }
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupButtons() {
        btnApplyCoupon.setOnClickListener(v -> applyCoupon());
        btnCheckout.setOnClickListener(v -> checkout());
    }
    
    private void applyCoupon() {
        if (CartCalculator.isCouponApplicable(cartItems)) {
            couponDiscount = CartCalculator.calculateCouponDiscount(cartItems);
            couponApplied = true;
            updateCartSummary();
            Snackbar.make(findViewById(android.R.id.content), 
                    String.format(COUPON_SUCCESS, couponDiscount),
                    Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(findViewById(android.R.id.content), 
                    CartCalculator.getCouponErrorMessage(cartItems), 
                    Snackbar.LENGTH_LONG).show();
        }
    }
    
    private void checkout() {
        if (cartItems.isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), 
                    "Your cart is empty", Snackbar.LENGTH_SHORT).show();
            return;
        }
        
        startActivity(new Intent(this, CheckoutActivity.class));
        MainActivity.clearCart();
        finish();
    }
    
    private void updateCartSummary() {
        if (cartItems.isEmpty()) {
            emptyCartView.setVisibility(View.VISIBLE);
            cartSummaryView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        
        emptyCartView.setVisibility(View.GONE);
        cartSummaryView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        
        double itemTotal = CartCalculator.calculateItemTotal(cartItems);
        double taxTotal = CartCalculator.calculateTaxTotal(cartItems);
        double subtotal = CartCalculator.calculateSubtotal(cartItems);
        
        if (!couponApplied || !CartCalculator.isCouponApplicable(cartItems)) {
            couponDiscount = 0.0;
        }
        
        double finalAmount = CartCalculator.calculateFinalAmount(cartItems, couponDiscount);
        
        tvItemTotal.setText(String.format(PRICE_FORMAT, itemTotal));
        tvTaxTotal.setText(String.format(PRICE_FORMAT, taxTotal));
        tvSubtotal.setText(String.format(PRICE_FORMAT, subtotal));
        tvCouponDiscount.setText(String.format("- " + PRICE_FORMAT, couponDiscount));
        tvFinalAmount.setText(String.format(PRICE_FORMAT, finalAmount));
        
        if (couponApplied) {
            btnApplyCoupon.setText(COUPON_APPLIED);
            btnApplyCoupon.setEnabled(false);
        } else {
            btnApplyCoupon.setText(APPLY_COUPON);
            btnApplyCoupon.setEnabled(true);
        }
    }
}
