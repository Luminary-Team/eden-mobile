package com.eden.api.dto;

import com.eden.model.Comment;

import java.util.List;

public class PostResponseMongo {

    private String id;
    private int userId;
    private String content;
    private List<Comment> comments;
    private List<Integer> engager;

    public PostResponseMongo(String id, int userId, String content, List<Comment> comments, List<Integer> engager) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.comments = comments;
        this.engager = engager;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUser() {
        return userId;
    }

    public void setUser(int user) {
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

    public List<Integer> getEngager() {
        return engager;
    }

    public void setEngager(List<Integer> engager) {
        this.engager = engager;
    }
}
