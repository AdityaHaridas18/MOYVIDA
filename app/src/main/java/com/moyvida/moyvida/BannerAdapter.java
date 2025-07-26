package com.moyvida.moyvida;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moyvida.moyvida.Movie;
import com.moyvida.moyvida.R;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

// BannerAdapter.java
public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private final List<Movie> bannerMovies;

    public BannerAdapter(List<Movie> bannerMovies) {
        this.bannerMovies = bannerMovies;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        if (bannerMovies == null || position >= bannerMovies.size()) {
            return;
        }

        Movie movie = bannerMovies.get(position);
        if (movie == null) {
            return;
        }

        // Load image using the poster URL from Movie object
        if (holder.itemView.getContext() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(movie.getPoster())
                    .placeholder(R.drawable.placeholder_banner)
                    .error(R.drawable.placeholder_banner)
                    .into(holder.bannerImage);
        }

        // Set the actual movie title with null check
        holder.bannerTitle.setText(movie.getTitle() != null ? movie.getTitle() : "");
        
        // Set genres if available with null checks
        if (movie.getGenres() != null && !movie.getGenres().isEmpty()) {
            String genresText = String.join(", ", movie.getGenres().keySet());
            holder.bannerGenres.setText(genresText);
        } else {
            holder.bannerGenres.setText("");
        }

        holder.itemView.setOnClickListener(v -> {
            if (movie.getTitle() == null || holder.itemView.getContext() == null) {
                return;
            }
            Intent intent = new Intent(holder.itemView.getContext(), MovieDetailActivity.class);
            intent.putExtra("movie_title", movie.getTitle());
            intent.putExtra("movie_poster", movie.getPoster() != null ? movie.getPoster() : "");
            intent.putExtra("movie_video", movie.getVideo() != null ? movie.getVideo() : "");
            intent.putExtra("movie_description", movie.getDescription() != null ? movie.getDescription() : "");
            intent.putExtra("movie_duration", movie.getDuration() != null ? movie.getDuration() : "");
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bannerMovies.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;
        TextView bannerTitle;
        TextView bannerGenres;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.bannerImage);
            bannerTitle = itemView.findViewById(R.id.bannerTitle);
            bannerGenres = itemView.findViewById(R.id.bannerGenres);
        }
    }
}