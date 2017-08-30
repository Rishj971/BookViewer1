package com.example.android.bookviewer;


public class Book {

    private String previewUrl;
    private String image;
    private String title;
    private String author;
    private String description;

    public Book(String previewUrl, String image, String title, String author, String description) {
        this.previewUrl = previewUrl;
        this.image = image;
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }
}

