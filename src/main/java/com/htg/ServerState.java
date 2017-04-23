package com.htg;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

@Component
public class ServerState {
    private ChallengeServer challengeServer = new ChallengeServer();
    private ChallengeDescription currentChallengeDescription = challengeServer.getNextChallengeDescription();
    private HashMap<String, User> sessionIds = new HashMap<>();
    private HashMap<String, User> users = new HashMap<>();
    private PriorityQueue<User> leaderboard = new PriorityQueue<>((o1, o2) -> o2.getScore() - o1.getScore());

    synchronized boolean addUser( String sessionId, User user ){
        // If server was empty, but user joined, load old scores
        if(sessionIds.size() == 0)
            loadStoredUsers();

        boolean newUser = !users.containsKey(user.getName()),
                inactiveUser = !sessionIds.containsKey(sessionId);

        if(!newUser && !inactiveUser)
            return false;
        if(newUser){
            users.put(user.getLowName(), user);
            leaderboard.add(user);
        }
        if(inactiveUser)
            sessionIds.put(sessionId, user);
        try {
            saveCurrentUsers();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    synchronized public void loadStoredUsers() {
        System.out.println("Loading users...");
        Scanner sc = null;
        try {
            sc = new Scanner(new File("storedUsers.json"));
            StringBuilder builder = new StringBuilder();
            while (sc.hasNextLine())
                builder.append(sc.nextLine());
            StoredUsers storedUsers = new Gson().fromJson(builder.toString(), StoredUsers.class);
            for (User u : storedUsers.getUsers() ){
                users.put(u.getLowName(), u);
                leaderboard.add(u);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    synchronized public void saveCurrentUsers() throws FileNotFoundException {
        System.out.println("Saving users...");
        StringBuilder builder = new StringBuilder();
        Gson gson = new Gson();
        ArrayList<User> tempLeaderboard = getLeaderboard();
        User[] users = new User[tempLeaderboard.size()];
        tempLeaderboard.toArray(users);
        builder.append("{").append("\"users\":").append(gson.toJson(users)).append("}");
        try( PrintWriter out = new PrintWriter( "storedUsers.json" ) ) { out.println( builder.toString() ); }
    }


    synchronized public ChallengeDescription getCurrentChallengeDescription() {
        return currentChallengeDescription;
    }


    synchronized public void goToNextChallenge() {
        currentChallengeDescription = challengeServer.getNextChallengeDescription();
    }
}


    class StoredUsers{
    private User[] users;

    public User[] getUsers() {
        return users;
    }
}
