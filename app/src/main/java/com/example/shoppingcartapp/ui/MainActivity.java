package com.example.shoppingcartapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingcartapp.R;
import com.example.shoppingcartapp.adapter.ProductAdapter;
import com.example.shoppingcartapp.data.ProductRepository;
import com.example.shoppingcartapp.model.CartItem;
import com.example.shoppingcartapp.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> products;
    private MaterialButton btnViewCart;
    private static final List<CartItem> cartItems = new ArrayList<>();
    private static final String VIEW_CART = "View Cart";
    private static final String VIEW_CART_FORMAT = "View Cart (%d)";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        initializeViews();
        loadProducts();
        setupRecyclerView();
        setupCartButton();
        updateCartCount();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_cart) {
            openCart();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void openCart() {
        startActivity(new Intent(this, CartActivity.class));
    }
    
    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewProducts);
        btnViewCart = findViewById(R.id.btnViewCart);
    }
    
    private void loadProducts() {
        products = ProductRepository.getInstance().getAllProducts();
    }
    
    private void setupRecyclerView() {
        adapter = new ProductAdapter(products, product -> {
            addToCart(product);
            updateCartCount();
            Snackbar.make(findViewById(android.R.id.content), 
                    new StringBuilder(product.getName()).append(" added to cart").toString(), 
                    Snackbar.LENGTH_SHORT)
                    .setAction("View Cart", v -> openCart())
                    .show();
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupCartButton() {
        btnViewCart.setOnClickListener(v -> openCart());
    }
    
    private void addToCart(Product product) {
        String productId = product.getId();
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                item.incrementQuantity();
                return;
            }
        }
        cartItems.add(new CartItem(product, 1));
    }
    
    private void updateCartCount() {
        int totalItems = 0;
        for (CartItem item : cartItems) {
            totalItems += item.getQuantity();
        }
        btnViewCart.setText(totalItems > 0 ? String.format(VIEW_CART_FORMAT, totalItems) : VIEW_CART);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }
    
    public static List<CartItem> getCartItems() {
        return cartItems;
    }
    
    public static void clearCart() {
        cartItems.clear();
    }
}

