package com.eden.model;

import java.util.List;

public class Post {
    private String id;
    private int userId;
    private String content;
    private List<Comment> comments;
    private String postDate;

    public Post(int userId, String content, List<Comment> comments, String postDate) {
        this.userId = userId;
        this.content = content;
        this.comments = comments;
        this.postDate = postDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
}
