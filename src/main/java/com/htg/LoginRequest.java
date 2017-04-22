package com.htg;

/**
 * Created by Morten on 22/04/2017.
 */
public class LoginRequest {
    private String username;

    public LoginRequest(){};

    public LoginRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
