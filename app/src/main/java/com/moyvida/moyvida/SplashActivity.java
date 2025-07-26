package com.moyvida.moyvida;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide action bar if present
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Delay for splash screen

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Check authentication state
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();

            if (currentUser != null) {
                // User is logged in - go to MainActivity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // User is not logged in - go to LoginActivity
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish(); // Close splash activity
        }, 2000); // 2 seconds delay
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Optional: You can move the auth check here if you want faster response
    }
}