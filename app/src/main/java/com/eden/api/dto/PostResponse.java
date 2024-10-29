package com.eden.api.dto;

import com.eden.model.Comment;

import java.util.List;

public class PostResponse {

    private int id;
    private UserSchema user;
    private String content;
    private List<Comment> comments;
    private List<Integer> likeId;

    public PostResponse(int id, UserSchema user, String content, List<Comment> comments, List<Integer> likeId) {
        this.id = id;
        this.user = user;
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

    public UserSchema getUser() {
        return user;
    }

    public void setUser(UserSchema user) {
        this.user = user;
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
