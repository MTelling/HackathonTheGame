package com.htg;

/**
 * Created by Morten on 27/04/2017.
 */
public class RunRequest {
    private String sessionID;
    private String code;
    private String challenge;

    public RunRequest(String sessionID, String code, String challenge) {
        this.sessionID = sessionID;
        this.code = code;
        this.challenge = challenge;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
}
