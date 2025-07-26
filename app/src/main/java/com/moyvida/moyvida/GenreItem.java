package com.moyvida.moyvida;

public class GenreItem {
    private final String name;
    private final int imageResId;

    public GenreItem(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}