package com.moyvida.moyvida;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private final List<GenreItem> genres;
    private final Context context;
    private final String selectedLanguage;

    public GenreAdapter(List<GenreItem> genres, Context context, String selectedLanguage) {
        this.genres = genres;
        this.context = context;
        this.selectedLanguage = selectedLanguage;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        GenreItem genre = genres.get(position);

        // Set genre image
        Glide.with(context)
                .load(genre.getImageResId())
                .into(holder.genreImage);

        // Set genre name
        holder.genreName.setText(genre.getName());

        holder.itemView.setOnClickListener(v -> {
            // Handle genre click
            Intent intent = new Intent(context, GenreMoviesActivity.class);
            intent.putExtra("genre", genre.getName());
            intent.putExtra("selectedLanguage", selectedLanguage);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        ImageView genreImage;
        TextView genreName;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            genreImage = itemView.findViewById(R.id.genreImage);
            genreName = itemView.findViewById(R.id.genreName);
        }
    }
}