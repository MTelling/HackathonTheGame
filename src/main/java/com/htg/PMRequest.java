package com.htg;

/**
 * Created by Morten on 22/04/2017.
 */
public class PMRequest {
    private String code;

    public PMRequest(){};

    public PMRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
