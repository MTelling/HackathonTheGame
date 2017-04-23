package com.htg;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

@Component
public class ServerState {
    private ChallengeServer challengeServer = new ChallengeServer();
    private ChallengeDescription currentChallengeDescription = challengeServer.getNextChallengeDescription();
    private HashMap<String, User> sessionIds = new HashMap<>();
    private HashMap<String, User> users = new HashMap<>();
    private PriorityQueue<User> leaderboard = new PriorityQueue<>((o1, o2) -> o2.getScore() - o1.getScore());

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
        leaderboard.add(user);

        return true;
    }

    synchronized User getUser (String sessionId) {
        if(sessionIds.containsKey(sessionId))
            return sessionIds.get(sessionId);
        return null;
    }

    synchronized public void updateUserScore(String sessionId, int i){
        User user = getUser(sessionId);
        leaderboard.remove(user);
        user.addScore(i);
        leaderboard.add(user);
    }

    synchronized ArrayList<User> getLeaderboard(){
        ArrayList<User> leaderboard = new ArrayList<>();
        User curr;
        while((curr = this.leaderboard.poll()) != null)
            leaderboard.add(curr);
        this.leaderboard.addAll(leaderboard);
        return leaderboard;
    }


    synchronized public ChallengeDescription getCurrentChallengeDescription() {
        return currentChallengeDescription;
    }


    synchronized public void goToNextChallenge() {
        currentChallengeDescription = challengeServer.getNextChallengeDescription();
    }
}

