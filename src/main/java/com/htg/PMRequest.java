package com.htg;

/**
 * Created by Morten on 22/04/2017.
 */
public class PMRequest {
    private String code;
    private String language = "Java";

    public PMRequest(){};

    public PMRequest(String code){
        this.code = code;
    }
    public PMRequest(String code, String language) {
        this.code = code;
        this.language = language;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }
}
