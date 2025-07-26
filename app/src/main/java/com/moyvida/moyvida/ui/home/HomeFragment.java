package com.moyvida.moyvida.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moyvida.moyvida.AllMoviesActivity;
import com.moyvida.moyvida.BannerAdapter;
import com.moyvida.moyvida.GenreAdapter;
import com.moyvida.moyvida.GenreItem;
import com.moyvida.moyvida.GenreMoviesActivity;
import com.moyvida.moyvida.Movie;
import com.moyvida.moyvida.MovieAdapter;
import com.moyvida.moyvida.ProfileActivity;
import com.moyvida.moyvida.R;
import com.moyvida.moyvida.databinding.FragmentHomeBinding;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HomeFragment extends Fragment {
    private ViewPager2 bannerViewPager;
    private DotsIndicator dotsIndicator;
    private RecyclerView trendingRecyclerView;
    private ImageView profile_image;
    private LinearLayout genresContainer;
    private Button allMoviesButton;
    RecyclerView genresRecyclerView;
    private DatabaseReference bannerRef;
    private DatabaseReference trendingRef;
    private DatabaseReference genresRef;
    private DatabaseReference languagesRef;
    private ValueEventListener bannerValueEventListener;
    private Spinner languageSpinner;
    private List<String> languages = new ArrayList<>();
    private ArrayAdapter<String> languageAdapter;
    private String currentLanguage = "All Languages";
    private List<Movie> allMovies = new ArrayList<>();
    private List<Movie> filteredMovies = new ArrayList<>();
    private MovieAdapter trendingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        profile_image = view.findViewById(R.id.profile_image);
        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        dotsIndicator = view.findViewById(R.id.dotsIndicator);
        trendingRecyclerView = view.findViewById(R.id.trendingRecyclerView);
        genresContainer = view.findViewById(R.id.genresContainer);
        genresRecyclerView = view.findViewById(R.id.genresRecyclerView);
        languageSpinner = view.findViewById(R.id.languageSpinner);

        // Initialize RecyclerViews
        if (trendingRecyclerView != null) {
            trendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false));
        }

        if (genresRecyclerView != null) {
            genresRecyclerView.setLayoutManager(new LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL, false));
        }

        // Setup click listeners
        if (profile_image != null) {
            profile_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);
                }
            });
        }

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        bannerRef = database.getReference("Banner");
        trendingRef = database.getReference("Trending");
        genresRef = database.getReference("All Movies");
        languagesRef = database.getReference("languages");

        // Setup language spinner
        setupLanguageSpinner();

        // Load data
        loadLanguages();
        loadBanners();
        loadTrending();
        loadGenres();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bannerRef != null && bannerValueEventListener != null) {
            bannerRef.removeEventListener(bannerValueEventListener);
        }
    }

    private void loadBanners() {
        if (getContext() == null) {
            return;
        }

        bannerValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    List<Movie> bannerMovies = new ArrayList<>();
                    for (DataSnapshot movieSnapshot : snapshot.getChildren()) {
                        Movie movie = movieSnapshot.getValue(Movie.class);
                        if (movie != null && movie.getPoster() != null && !movie.getPoster().isEmpty()) {
                            bannerMovies.add(movie);
                        }
                    }

                    if (bannerMovies.isEmpty()) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "No banner data available", 
                                Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    if (getContext() != null) {
                        BannerAdapter adapter = new BannerAdapter(bannerMovies);
                        bannerViewPager.setAdapter(adapter);
                        dotsIndicator.setViewPager2(bannerViewPager);
                    }
                } catch (Exception e) {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error loading banners: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to load banners: " + error.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            }
        };

        bannerRef.addValueEventListener(bannerValueEventListener);
    }

    private void loadTrending() {
        if (trendingRecyclerView == null || getContext() == null) {
            return;
        }

        trendingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    List<Movie> trendingMovies = new ArrayList<>();
                    for (DataSnapshot movieSnapshot : snapshot.getChildren()) {
                        Movie movie = movieSnapshot.getValue(Movie.class);
                        if (movie != null) {
                            trendingMovies.add(movie);
                        }
                    }

                    if (trendingMovies.isEmpty() && getContext() != null) {
                        Toast.makeText(getContext(), "No trending movies available", 
                            Toast.LENGTH_SHORT).show();
                    }

                    if (getContext() != null && trendingRecyclerView != null) {
                        trendingAdapter = new MovieAdapter(trendingMovies, false, requireContext());
                        trendingRecyclerView.setAdapter(trendingAdapter);
                    }
                } catch (Exception e) {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error loading trending movies: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to load trending: " + error.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadGenres() {
        if (genresRecyclerView == null || getContext() == null) {
            return;
        }

        List<GenreItem> genres = new ArrayList<>();
        genres.add(new GenreItem("Action", R.drawable.action));
        genres.add(new GenreItem("Comedy", R.drawable.comedy));
        genres.add(new GenreItem("Horror", R.drawable.horror));
        genres.add(new GenreItem("Romance", R.drawable.romantic));
        genres.add(new GenreItem("Suspense", R.drawable.suspense));
        genres.add(new GenreItem("Thriller", R.drawable.thriller));

        GenreAdapter adapter = new GenreAdapter(genres, requireContext(), currentLanguage);
        genresRecyclerView.setAdapter(adapter);

        // Update genre visibility based on available movies in selected language
        updateGenreVisibility(genres);
    }

    private void updateGenreVisibility(List<GenreItem> allGenres) {
        if (genresRecyclerView == null || getContext() == null) {
            return;
        }

        if (currentLanguage.equals("All Languages")) {
            // For "All Languages", show no genres
            GenreAdapter adapter = new GenreAdapter(new ArrayList<>(), requireContext(), currentLanguage);
            genresRecyclerView.setAdapter(adapter);
            return;
        }

        // Reference to the Genres node
        DatabaseReference genresRef = FirebaseDatabase.getInstance().getReference("Genres");
        
        genresRef.child(currentLanguage).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    Set<String> availableGenres = new HashSet<>();
                    
                    // Debug log
                    Log.d("HomeFragment", "Loading genres for language: " + currentLanguage);
                    Log.d("HomeFragment", "Number of genre entries: " + snapshot.getChildrenCount());
                    
                    // Directly get genres for the selected language
                    for (DataSnapshot genreSnapshot : snapshot.getChildren()) {
                        String genreName = genreSnapshot.getKey();
                        Boolean isAvailable = genreSnapshot.getValue(Boolean.class);
                        Log.d("HomeFragment", "Genre: " + genreName + ", Available: " + isAvailable);
                        
                        if (isAvailable != null && isAvailable && genreName != null) {
                            availableGenres.add(genreName.toLowerCase());
                        }
                    }

                    Log.d("HomeFragment", "Available genres: " + availableGenres.toString());

                    // Filter the adapter's data to only show available genres
                    List<GenreItem> filteredGenres = new ArrayList<>();
                    for (GenreItem genre : allGenres) {
                        String genreName = genre.getName().toLowerCase();
                        if (availableGenres.contains(genreName)) {
                            filteredGenres.add(genre);
                            Log.d("HomeFragment", "Added genre to filtered list: " + genre.getName());
                        }
                    }

                    if (filteredGenres.isEmpty()) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), 
                                "No genres available for " + currentLanguage, 
                                Toast.LENGTH_SHORT).show();
                        }
                        Log.d("HomeFragment", "No genres available for " + currentLanguage);
                    }

                    // Update the RecyclerView with filtered genres and pass the selected language
                    if (getContext() != null) {
                        GenreAdapter adapter = new GenreAdapter(filteredGenres, requireContext(), currentLanguage);
                        genresRecyclerView.setAdapter(adapter);
                        Log.d("HomeFragment", "Updated adapter with " + filteredGenres.size() + " genres");
                    }

                } catch (Exception e) {
                    Log.e("HomeFragment", "Error updating genres: " + e.getMessage(), e);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), 
                            "Error updating genres: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment", "Failed to update genres: " + error.getMessage());
                if (getContext() != null) {
                    Toast.makeText(getContext(), 
                        "Failed to update genres: " + error.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void filterContent() {
        // Only update genre visibility when language changes
        List<GenreItem> genres = new ArrayList<>();
        genres.add(new GenreItem("Action", R.drawable.action));
        genres.add(new GenreItem("Comedy", R.drawable.comedy));
        genres.add(new GenreItem("Horror", R.drawable.horror));
        genres.add(new GenreItem("Romance", R.drawable.romantic));
        genres.add(new GenreItem("Suspense", R.drawable.suspense));
        genres.add(new GenreItem("Thriller", R.drawable.thriller));
        updateGenreVisibility(genres);
    }

    private void setupLanguageSpinner() {
        if (languageSpinner == null || getContext() == null) {
            return;
        }

        languages.clear();
        languages.add("All Languages"); // Add default option
        languageAdapter = new ArrayAdapter<>(requireContext(),
                R.layout.spinner_item, languages);
        languageAdapter.setDropDownViewResource(R.layout.spinner_item);
        languageSpinner.setAdapter(languageAdapter);
        
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < languages.size()) {
                    String newLanguage = languages.get(position);
                    Log.d("HomeFragment", "Selected language: " + newLanguage);
                    
                    if (!newLanguage.equals(currentLanguage)) {
                        currentLanguage = newLanguage;
                        Log.d("HomeFragment", "Language changed to: " + currentLanguage);
                        filterContent();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadLanguages() {
        if (getContext() == null) {
            return;
        }

        languagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                languages.clear();
                languages.add("All Languages"); // Default option
                
                for (DataSnapshot languageSnapshot : snapshot.getChildren()) {
                    String languageValue = languageSnapshot.getValue(String.class);
                    if (languageValue != null && !languageValue.isEmpty()) {
                        // Add exact language string from Firebase
                        languages.add(languageValue);
                        Log.d("HomeFragment", "Added language: " + languageValue);
                    }
                }
                
                if (languageAdapter != null) {
                    languageAdapter.notifyDataSetChanged();
                }

                // If current language is not in the list anymore, reset to "All Languages"
                if (!languages.contains(currentLanguage)) {
                    currentLanguage = "All Languages";
                    if (languageSpinner != null) {
                        languageSpinner.setSelection(0);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("HomeFragment", "Failed to load languages: " + error.getMessage());
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to load languages: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
