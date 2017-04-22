package com.htg;

import java.util.ArrayList;

/**
 * Created by Morten on 22/04/2017.
 */
public class NewsResponse {
    private String type;
    private String message;
    private ArrayList<User> leaderboard;

    public NewsResponse(){};

    public NewsResponse(String type, String message) {
        this.type = type;
        this.message = message;
    }
    public NewsResponse(String type, String message, ArrayList<User> scoreboard) {
        this.type = type;
        this.message = message;
        this.leaderboard = scoreboard;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<User> getLeaderboard() {
        return leaderboard;
    }
}
