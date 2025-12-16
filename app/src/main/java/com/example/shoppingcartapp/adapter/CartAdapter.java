package com.example.shoppingcartapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingcartapp.R;
import com.example.shoppingcartapp.model.CartItem;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final List<CartItem> cartItems;
    private final OnQuantityChangeListener listener;
    
    public interface OnQuantityChangeListener {
        void onQuantityChanged();
        void onItemRemoved(int position);
    }
    
    public CartAdapter(List<CartItem> cartItems, OnQuantityChangeListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(cartItems.get(position), position);
    }
    
    @Override
    public int getItemCount() {
        return cartItems.size();
    }
    
    class CartViewHolder extends RecyclerView.ViewHolder {
        private final TextView productName;
        private final TextView productPrice;
        private final TextView quantity;
        private final TextView itemTotal;
        private final TextView taxInfo;
        private final ImageButton btnDecrease;
        private final ImageButton btnIncrease;
        private final ImageButton btnRemove;
        private final StringBuilder priceBuilder;
        
        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvCartProductName);
            productPrice = itemView.findViewById(R.id.tvCartProductPrice);
            quantity = itemView.findViewById(R.id.tvQuantity);
            itemTotal = itemView.findViewById(R.id.tvItemTotal);
            taxInfo = itemView.findViewById(R.id.tvTaxInfo);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            priceBuilder = new StringBuilder(20);
        }
        
        void bind(CartItem cartItem, int position) {
            productName.setText(cartItem.getProduct().getName());
            
            priceBuilder.setLength(0);
            if (cartItem.getProduct().isDiscounted()) {
                priceBuilder.append("₹").append(String.format("%.2f", cartItem.getProduct().getDiscountedPrice()))
                        .append(" (Discounted)");
            } else {
                priceBuilder.append("₹").append(String.format("%.2f", cartItem.getProduct().getEffectivePrice()));
            }
            productPrice.setText(priceBuilder.toString());
            
            quantity.setText(String.valueOf(cartItem.getQuantity()));
            
            priceBuilder.setLength(0);
            priceBuilder.append("₹").append(String.format("%.2f", cartItem.getItemTotal()));
            itemTotal.setText(priceBuilder.toString());
            
            priceBuilder.setLength(0);
            priceBuilder.append("Tax (").append(cartItem.getProduct().getTaxGroup())
                    .append("%): ₹").append(String.format("%.2f", cartItem.getTaxAmount()));
            taxInfo.setText(priceBuilder.toString());
            
            btnDecrease.setOnClickListener(v -> {
                cartItem.decrementQuantity();
                notifyItemChanged(position);
                if (listener != null) {
                    listener.onQuantityChanged();
                }
            });
            
            btnIncrease.setOnClickListener(v -> {
                cartItem.incrementQuantity();
                notifyItemChanged(position);
                if (listener != null) {
                    listener.onQuantityChanged();
                }
            });
            
            btnRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemRemoved(position);
                }
            });
        }
    }
}
