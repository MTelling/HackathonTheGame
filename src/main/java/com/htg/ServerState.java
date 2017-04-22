package com.htg;

import java.util.HashMap;
import java.util.function.Function;

public class ServerState {
    private Game currentGame = null;
    private HashMap<String, User> users = new HashMap<>();
    /**
     * TODO: maybe add time limit on challenges?
     */
    private long challengeStartTime;
    private long challengeEndTime;

    synchronized void addUser( User user ){
        if(users.containsKey(user.getName()))
            return;
        users.put(user.getName(), user);
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }
}

class User {

    private String name;

    User(String name) {
        this.name = name;
    }

    public String getName(){ return name; }
}