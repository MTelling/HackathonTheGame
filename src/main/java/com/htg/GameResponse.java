package com.htg;

/**
 * Created by Morten on 22/04/2017.
 */
public class GameResponse {

    private String status;

    public GameResponse(){};

    public GameResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
