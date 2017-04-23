package com.htg;

/**
 * Created by Morten on 22/04/2017.
 */
public class LoginResponse {

    private String status;
    private String username;
    private int score;

    public LoginResponse(){};

    public LoginResponse(String status, String username, int score) {
        this.status = status;
        this.username = username;
        this.score = score;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
