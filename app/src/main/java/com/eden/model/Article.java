package com.eden.model;

import org.w3c.dom.Comment;

import java.util.Date;
import java.util.List;

public class Article {

    private String id;
    private String url;
    private String title;
    private String description;
    private String date;

    public Article(String id, String url, String title, String description, String date) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
// Constructors, getters, and setters can be added here
}
