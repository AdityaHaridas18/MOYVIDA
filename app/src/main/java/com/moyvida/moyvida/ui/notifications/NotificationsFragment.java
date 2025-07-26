package com.moyvida.moyvida.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moyvida.moyvida.Movie;
import com.moyvida.moyvida.MovieAdapter;
import com.moyvida.moyvida.R;
import com.moyvida.moyvida.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends Fragment {
    private RecyclerView moviesRecyclerView;
    private ProgressBar progressBar;
    private MovieAdapter adapter;
    private List<Movie> movieList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        moviesRecyclerView = view.findViewById(R.id.rvAllMovies);
        progressBar = view.findViewById(R.id.progressBar);

        // Setup RecyclerView
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new MovieAdapter(movieList, true, requireContext());
        moviesRecyclerView.setAdapter(adapter);

        loadAllMovies();

        return view;
    }

    private void loadAllMovies() {
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference moviesRef = FirebaseDatabase.getInstance().getReference("Trending");
        moviesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movieList.clear();
                for (DataSnapshot movieSnapshot : snapshot.getChildren()) {
                    Movie movie = movieSnapshot.getValue(Movie.class);
                    if (movie != null) {
                        movieList.add(movie);
                    }
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to load movies: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}