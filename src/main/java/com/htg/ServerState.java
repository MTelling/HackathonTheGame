package com.htg;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

@Component
public class ServerState {
    private Game currentGame = null;
    private HashMap<String, User> sessionIds = new HashMap<>();
    private HashMap<String, User> users = new HashMap<>();
    /**
     * TODO: maybe add time limit on challenges?
     */
    private long challengeStartTime;
    private long challengeEndTime;

    synchronized boolean addUser( String sessionId, User user ){
        if(users.containsKey(user.getName()))
            return false;
        users.put(user.getName(), user);
        sessionIds.put(sessionId, user);
        return true;
    }

    User getUser (String sessionId) {
        if(sessionIds.containsKey(sessionId))
            return sessionIds.get(sessionId);
        return null;
    }

    synchronized public Game getCurrentGame () {
        return currentGame;
    }

    synchronized public void setCurrentGame (Game currentGame) {
        this.currentGame = currentGame;
    }
}

