package com.eden.model;

public class Comment {
    public int userId;
    public String content;

    public Comment(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
