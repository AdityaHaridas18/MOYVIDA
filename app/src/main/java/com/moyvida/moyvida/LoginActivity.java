package com.moyvida.moyvida;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity {
    private EditText etLoginId, etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etLoginId = findViewById(R.id.etLoginId);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvSignUp = findViewById(R.id.tvSignUp);

        btnLogin.setOnClickListener(v -> loginUser());
        tvSignUp.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
    }

    private void loginUser() {
        String loginId = etLoginId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (loginId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if loginId is email or phone
        if (loginId.contains("@")) {
            // Email login
            mAuth.signInWithEmailAndPassword(loginId, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Login failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Phone login - You'll need to implement phone auth separately
            Toast.makeText(this, "Phone login coming soon", Toast.LENGTH_SHORT).show();
        }
    }

    }