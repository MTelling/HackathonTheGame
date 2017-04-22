package com.htg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Morten on 22/04/2017.
 */
public class ChallengeServer {

    private final String pathToChallenges = "Testing/Tests/";
    private final String fileType = ".json";
    private Queue<String> challengeNames = new LinkedList<>();

    public ChallengeServer() {
        challengeNames.add("Challenge_One");
        challengeNames.add("Challenge_Two");
        challengeNames.add("Challenge_Three");
        challengeNames.add("Challenge_Four");
    }

    public ChallengeDescription getNextChallengeDescription() {
        String challengeName = challengeNames.poll();
        challengeNames.add(challengeName);
        ChallengeReader challengeReader = new ChallengeReader();

        try {
            return challengeReader.getChallengeDescriptionFromPath(createPath(challengeName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String createPath(String challenge) {
        return pathToChallenges + challenge + fileType;
    }

}
