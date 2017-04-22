package com.htg;

public class GameResponse {

    private Game status;

    public GameResponse(){};

    public GameResponse(Game status) {
        this.status = status;
    }

    public Game getStatus() {
        return status;
    }

    public void setStatus(Game status) {
        this.status = status;
    }

}
