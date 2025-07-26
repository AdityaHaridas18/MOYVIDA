package com.moyvida.moyvida;

import java.io.Serializable;
import java.util.Map;

public class Movie implements Serializable {
    private String title;
    private String description;
    private String duration;
    private Map<String, Boolean> genres;
    private String poster;
    private String video;
    private boolean isFavorite;
    private String language;

    // Default constructor required for Firebase
    public Movie() {
    }

    public Movie(String title, String description, String duration,
                 Map<String, Boolean> genres, String poster, String video, String language) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.genres = genres;
        this.poster = poster;
        this.video = video;
        this.language = language;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Map<String, Boolean> getGenres() {
        return genres;
    }

    public void setGenres(Map<String, Boolean> genres) {
        this.genres = genres;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
