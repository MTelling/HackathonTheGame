package com.htg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Morten on 22/04/2017.
 */
public class ChallengeServer {

    private final String pathToChallenges = "Testing/Tests/";
    private Queue<String> challengeNames = new LinkedList<>();

    public ChallengeServer() {
        try {
            Files.newDirectoryStream(Paths.get(pathToChallenges), path -> path.toFile().isFile())
                    .forEach(path -> challengeNames.add(path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        return pathToChallenges + challenge;
    }

}
