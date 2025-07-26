package com.moyvida.moyvida;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
// In MovieDetailActivity.java
public class MovieDetailActivity extends AppCompatActivity {
    private ImageView moviePoster;
    private TextView movieTitle, movieDescription, movieDuration;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_movie_detail);

        // Initialize views
        moviePoster = findViewById(R.id.moviePoster);
        movieTitle = findViewById(R.id.movieTitle);
        movieDescription = findViewById(R.id.movieDescription);
        movieDuration = findViewById(R.id.movieDuration);
        playButton = findViewById(R.id.playButton);

        // Get movie data from intent
        String title = getIntent().getStringExtra("movie_title");
        String posterUrl = getIntent().getStringExtra("movie_poster");
        String videoUrl = getIntent().getStringExtra("movie_video");
        String description = getIntent().getStringExtra("movie_description");
        String duration = getIntent().getStringExtra("movie_duration");

        // Set movie data
        movieTitle.setText(title);
        movieDescription.setText(description);
        movieDuration.setText(duration);

        // Load poster image
        Glide.with(this)
                .load(posterUrl)
                .into(moviePoster);

        // Handle play button click
        playButton.setOnClickListener(v -> {
            if (videoUrl != null && !videoUrl.isEmpty()) {
                Intent playerIntent = new Intent(this, PlayerActivity.class);
                playerIntent.putExtra("video_url", videoUrl);
                playerIntent.putExtra("movie_title", title);
                startActivity(playerIntent);
            } else {
                Toast.makeText(this, "Video not available", Toast.LENGTH_SHORT).show();
            }
        });
    }
}