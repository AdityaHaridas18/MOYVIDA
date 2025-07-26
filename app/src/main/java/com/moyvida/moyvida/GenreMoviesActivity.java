package com.moyvida.moyvida;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import java.util.Map;

public class GenreMoviesActivity extends AppCompatActivity {
    private RecyclerView moviesRecyclerView;
    private MovieAdapter movieAdapter;
    private String selectedGenre;
    private String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_movies);

        // Get the selected genre and language from intent
        selectedGenre = getIntent().getStringExtra("genre");
        selectedLanguage = getIntent().getStringExtra("selectedLanguage");

        // Initialize views
        moviesRecyclerView = findViewById(R.id.moviesRecyclerView);
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(selectedGenre + " Movies (" + selectedLanguage + ")");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Load movies for the selected genre and language
        loadMoviesForGenre();
    }

    private void loadMoviesForGenre() {
        DatabaseReference moviesRef = FirebaseDatabase.getInstance().getReference("All Movies");
        
        Log.d("GenreMoviesActivity", "Loading movies for genre: " + selectedGenre + " in language: " + selectedLanguage);
        
        moviesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Movie> genreMovies = new ArrayList<>();
                
                for (DataSnapshot movieSnapshot : snapshot.getChildren()) {
                    try {
                        Movie movie = movieSnapshot.getValue(Movie.class);
                        if (movie == null || movie.getGenres() == null) {
                            continue;
                        }

                        // Strict language check
                        String movieLanguage = movie.getLanguage();
                        if (movieLanguage == null || !movieLanguage.equals(selectedLanguage)) {
                            // Skip if language doesn't match exactly
                            Log.d("GenreMoviesActivity", "Skipping movie: " + movie.getTitle() + 
                                " - Movie language: " + movieLanguage + 
                                " - Selected language: " + selectedLanguage);
                            continue;
                        }

                        // Check genre
                        Map<String, Boolean> genres = movie.getGenres();
                        String genreLowerCase = selectedGenre.toLowerCase();
                        Boolean hasGenre = genres.get(genreLowerCase);
                        
                        if (hasGenre != null && hasGenre) {
                            genreMovies.add(movie);
                            Log.d("GenreMoviesActivity", "Added movie: " + movie.getTitle() + 
                                " - Language: " + movieLanguage);
                        }

                    } catch (Exception e) {
                        Log.e("GenreMoviesActivity", "Error parsing movie: " + e.getMessage(), e);
                    }
                }

                Log.d("GenreMoviesActivity", "Total movies found: " + genreMovies.size() + 
                    " for language: " + selectedLanguage);

                if (genreMovies.isEmpty()) {
                    Toast.makeText(GenreMoviesActivity.this, 
                        "No " + selectedGenre + " movies available in " + selectedLanguage, 
                        Toast.LENGTH_SHORT).show();
                }

                movieAdapter = new MovieAdapter(genreMovies, true, GenreMoviesActivity.this);
                moviesRecyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("GenreMoviesActivity", "Database error: " + error.getMessage());
                Toast.makeText(GenreMoviesActivity.this, 
                    "Failed to load movies: " + error.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}