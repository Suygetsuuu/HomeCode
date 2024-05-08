package com.example.demo.controller.dto;

import java.io.Serializable;
import java.util.List;

public class UserResourcesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    List<String> endpoint;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(List<String> endpoint) {
        this.endpoint = endpoint;
    }
}
