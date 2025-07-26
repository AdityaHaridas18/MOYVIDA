package com.moyvida.moyvida;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final List<Movie> movies;
    private final boolean isVertical;
    private final Context context;


    public MovieAdapter(List<Movie> movies, boolean isVertical, Context context) {
        this.movies = movies;
        this.isVertical = isVertical;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = isVertical ? R.layout.item_movie_vertical : R.layout.item_movie_horizontal;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        Glide.with(context)
                .load(movie.getPoster())  // URL from Movie object
                .into(holder.moviePoster);

        // Set the actual movie title
        holder.movieTitle.setText(movie.getTitle());

        // For vertical layout, show duration if available
        if (isVertical && holder.movieDuration != null) {
            holder.movieDuration.setText(movie.getDuration());
        }

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra("movie_title", movie.getTitle());
            intent.putExtra("movie_poster", movie.getPoster());
            intent.putExtra("movie_video", movie.getVideo());
            intent.putExtra("movie_description", movie.getDescription());
            intent.putExtra("movie_duration", movie.getDuration());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieTitle;
        TextView movieDuration;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieDuration = itemView.findViewById(R.id.movieDuration);
        }
    }
}
