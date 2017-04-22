package com.htg;

public class GameResponse {

    private Challenge status;

    public GameResponse(){};

    public GameResponse(Challenge status) {
        this.status = status;
    }

    public Challenge getStatus() {
        return status;
    }

    public void setStatus(Challenge status) {
        this.status = status;
    }

}
