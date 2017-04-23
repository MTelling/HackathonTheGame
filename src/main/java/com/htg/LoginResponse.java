package com.htg;

import java.util.ArrayList;

/**
 * Created by Morten on 22/04/2017.
 */
public class LoginResponse {

    private ArrayList<User> leaderboard;
    private String status;
    private String username;
    private int score;


    public LoginResponse(){};

    public LoginResponse(String status, String username, int score, ArrayList<User> leaderboard) {
        this.status = status;
        this.username = username;
        this.score = score;
        this.leaderboard = leaderboard;
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

    public ArrayList<User> getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(ArrayList<User> leaderboard) {
        this.leaderboard = leaderboard;
    }
}
