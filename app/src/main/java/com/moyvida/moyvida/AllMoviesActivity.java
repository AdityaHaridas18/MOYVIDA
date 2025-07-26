package com.moyvida.moyvida;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllMoviesActivity extends AppCompatActivity {
    private RecyclerView moviesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_all_movies);

        moviesRecyclerView = findViewById(R.id.moviesRecyclerView);
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        loadAllMovies();
    }

    private void loadAllMovies() {
        DatabaseReference moviesRef = FirebaseDatabase.getInstance().getReference("All Movies");

        moviesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Movie> allMovies = new ArrayList<>();
                for (DataSnapshot movieSnapshot : snapshot.getChildren()) {
                    Movie movie = movieSnapshot.getValue(Movie.class);
                    allMovies.add(movie);
                }

                MovieAdapter adapter = new MovieAdapter(allMovies, true,AllMoviesActivity.this);
                moviesRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllMoviesActivity.this, "Failed to load movies", Toast.LENGTH_SHORT).show();
            }
        });
    }
}