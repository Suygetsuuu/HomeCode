package com.example.demo.controller.domain;

import com.example.demo.controller.Role;

public class User {
    private Long userId;

    private String accountName;

    private Role role;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User(Long userId, String accountName, Role role) {
        this.userId = userId;
        this.accountName = accountName;
        this.role = role;
    }

    public User() {

    }
}
