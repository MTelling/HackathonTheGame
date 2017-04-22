package com.htg;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ServerState {
    private Challenge currentChallenge = new Challenge("lol","This is a test challenge for testing");
    private HashMap<String, User> sessionIds = new HashMap<>();
    private HashMap<String, User> users = new HashMap<>();
    /**
     * TODO: maybe add time limit on challenges?
     */
    private long challengeStartTime;
    private long challengeEndTime;

    synchronized boolean addUser( String sessionId, User user ){
        String usernameInLower = user.getName().toLowerCase();

        if(users.containsKey(user.getName()))
        return false;

        users.put(usernameInLower, user);
        sessionIds.put(sessionId, user);

        return true;
    }

    synchronized User getUser (String sessionId) {
        if(sessionIds.containsKey(sessionId))
            return sessionIds.get(sessionId);
        return null;
    }

    synchronized public Challenge getCurrentChallenge() {
        return currentChallenge;
    }

    synchronized public void setCurrentChallenge(Challenge currentChallenge) {
        this.currentChallenge = currentChallenge;
    }
}

