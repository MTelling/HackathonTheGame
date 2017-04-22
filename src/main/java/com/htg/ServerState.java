package com.htg;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

@Component
public class ServerState {
    private Game currentGame = null;
    private HashMap<String, User> users = new HashMap<>();
    /**
     * TODO: maybe add time limit on challenges?
     */
    private long challengeStartTime;
    private long challengeEndTime;

    synchronized boolean addUser( User user ){
        if(users.containsKey(user.getName()))
            return false;
        users.put(user.getName (), user);
        return true;
    }


    synchronized public Game getCurrentGame () {
        return currentGame;
    }

    synchronized public void setCurrentGame (Game currentGame) {
        this.currentGame = currentGame;
    }
}

