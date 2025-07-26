package com.moyvida.moyvida;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
// In PlayerActivity.java
public class PlayerActivity extends AppCompatActivity {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_player);

        playerView = findViewById(R.id.playerView);
        titleTextView = findViewById(R.id.titleTextView);

        String videoUrl = getIntent().getStringExtra("video_url");
        String title = getIntent().getStringExtra("movie_title");

        // Set movie title
        titleTextView.setText(title);

        if (videoUrl != null) {
            initializePlayer(videoUrl);
        }

        // Close button
        findViewById(R.id.btnClose).setOnClickListener(v -> finish());
    }

    private void initializePlayer(String videoUrl) {
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}