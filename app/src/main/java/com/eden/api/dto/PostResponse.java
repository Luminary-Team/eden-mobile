package com.eden.api.dto;

import com.eden.model.Comment;

import java.util.List;

public class PostResponse {

    private String id;
    private UserSchema user;
    private String content;
    private List<Comment> comments;
    private List<Integer> engager;

    public PostResponse(String id, UserSchema user, String content, List<Comment> comments, List<Integer> engager) {
        this.id = id;
        this.user = user;
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

    public List<Integer> getEngager() {
        return engager;
    }

    public void setEngager(List<Integer> engager) {
        this.engager = engager;
    }
}
