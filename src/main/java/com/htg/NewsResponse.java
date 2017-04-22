package com.htg;

/**
 * Created by Morten on 22/04/2017.
 */
public class NewsResponse {
    private String type;
    private String message;

    public NewsResponse(){};

    public NewsResponse(String type, String message) {

        this.type = type;
        this.message = message;
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
}
