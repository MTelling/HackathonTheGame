package com.htg;

/**
 * Created by Morten on 22/04/2017.
 */
public class PMResponse {
    private String message;

    public PMResponse() {};

    public PMResponse(String status) {
        this.message = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
