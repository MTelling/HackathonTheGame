package com.htg;

/**
 * Created by Morten on 22/04/2017.
 */
public class LoginResponse {

    private String status;
    private String username;

    public LoginResponse(){};

    public LoginResponse(String status, String username) {
        this.status = status;
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
