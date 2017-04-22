package com.htg;

/**
 * Created by Morten on 22/04/2017.
 */
public class PMResponse {
    private String status;

    public PMResponse() {};

    public PMResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
