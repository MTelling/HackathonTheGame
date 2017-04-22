package com.htg;

/**
 * Created by Morten on 22/04/2017.
 */
public class LoginResponse {

    private String status;

    public LoginResponse(){};

    public LoginResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
