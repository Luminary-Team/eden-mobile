package com.eden.api.dto;

import com.eden.model.Comment;

import java.util.List;

public class PostResponseMongo {

    private int id;
    private int userId;
    private String content;
    private List<Comment> comments;
    private List<Integer> likeId;

    public PostResponseMongo(int id, int userId, String content, List<Comment> comments, List<Integer> likeId) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.comments = comments;
        this.likeId = likeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<Integer> getLikeId() {
        return likeId;
    }

    public void setLikeId(List<Integer> likeId) {
        this.likeId = likeId;
    }
}
