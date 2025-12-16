package com.example.shoppingcartapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingcartapp.R;
import com.example.shoppingcartapp.model.Product;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Product> products;
    private final OnAddToCartListener listener;
    
    public interface OnAddToCartListener {
        void onAddToCart(Product product);
    }
    
    public ProductAdapter(List<Product> products, OnAddToCartListener listener) {
        this.products = products;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position));
    }
    
    @Override
    public int getItemCount() {
        return products.size();
    }
    
    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView productName;
        private final TextView originalPrice;
        private final TextView discountedPrice;
        private final TextView taxGroup;
        private final Button addToCartButton;
        private final int discountColor;
        private final int blackColor;
        
        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvProductName);
            originalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            discountedPrice = itemView.findViewById(R.id.tvDiscountedPrice);
            taxGroup = itemView.findViewById(R.id.tvTaxGroup);
            addToCartButton = itemView.findViewById(R.id.btnAddToCart);
            discountColor = itemView.getContext().getColor(R.color.discount_green);
            blackColor = itemView.getContext().getColor(android.R.color.black);
        }
        
        void bind(Product product) {
            productName.setText(product.getName());
            taxGroup.setText(new StringBuilder("Tax: ").append(product.getTaxGroup()).append("%").toString());
            
            if (product.isDiscounted()) {
                originalPrice.setText(new StringBuilder("₹").append(String.format("%.2f", product.getOriginalPrice())).toString());
                originalPrice.setVisibility(View.VISIBLE);
                originalPrice.setPaintFlags(originalPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
                discountedPrice.setText(new StringBuilder("₹").append(String.format("%.2f", product.getDiscountedPrice())).toString());
                discountedPrice.setVisibility(View.VISIBLE);
                discountedPrice.setTextColor(discountColor);
            } else {
                originalPrice.setVisibility(View.GONE);
                discountedPrice.setText(new StringBuilder("₹").append(String.format("%.2f", product.getOriginalPrice())).toString());
                discountedPrice.setVisibility(View.VISIBLE);
                discountedPrice.setTextColor(blackColor);
            }
            
            addToCartButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddToCart(product);
                }
            });
        }
    }
}
