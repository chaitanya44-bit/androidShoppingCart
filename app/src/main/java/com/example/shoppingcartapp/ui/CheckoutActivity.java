package com.example.shoppingcartapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.example.shoppingcartapp.R;
import com.google.android.material.button.MaterialButton;

public class CheckoutActivity extends AppCompatActivity {
    private LottieAnimationView lottieAnimation;
    private MaterialButton btnContinueShopping;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        
        initializeViews();
        setupAnimation();
        setupButton();
    }
    
    private void initializeViews() {
        lottieAnimation = findViewById(R.id.lottieAnimation);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);
    }
    
    private void setupAnimation() {
        try {
            lottieAnimation.setAnimation(R.raw.confetti);
            lottieAnimation.playAnimation();
            lottieAnimation.loop(true);
        } catch (Exception e) {
            lottieAnimation.setVisibility(View.GONE);
        }
    }
    
    private void setupButton() {
        btnContinueShopping.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lottieAnimation != null) {
            lottieAnimation.cancelAnimation();
        }
    }
}
