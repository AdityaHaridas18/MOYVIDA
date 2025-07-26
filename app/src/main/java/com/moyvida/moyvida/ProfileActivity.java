package com.moyvida.moyvida;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.moyvida.moyvida.AllMoviesActivity;
import com.moyvida.moyvida.BannerAdapter;
import com.moyvida.moyvida.GenreAdapter;
import com.moyvida.moyvida.GenreItem;
import com.moyvida.moyvida.GenreMoviesActivity;
import com.moyvida.moyvida.Movie;
import com.moyvida.moyvida.MovieAdapter;
import com.moyvida.moyvida.R;
import com.moyvida.moyvida.databinding.FragmentHomeBinding;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvName, tvEmail, tvMobile;
    private ImageView ivProfile;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvMobile = findViewById(R.id.tvPhoneNumber);
        ivProfile = findViewById(R.id.ivProfileImage);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String mobile = snapshot.child("mobile").getValue(String.class);
                    String imageUrl = snapshot.child("profileImage").getValue(String.class);

                    tvName.setText(name);
                    tvEmail.setText(email);
                    tvMobile.setText(mobile);

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(ProfileActivity.this).load(imageUrl).into(ivProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}