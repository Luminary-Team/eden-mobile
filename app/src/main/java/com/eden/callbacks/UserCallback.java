package com.eden.callbacks;

import com.eden.api.dto.UserSchema;

public interface UserCallback {
    void setResponse(UserSchema response);
}