package com.eden.model;

import com.eden.api.dto.UserSchema;

public class Comment {
    public UserSchema user;
    public String content;

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
}
