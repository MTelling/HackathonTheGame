package com.htg;

import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.boot.json.GsonJsonParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * Created by Morten on 22/04/2017.
 */
public class RunTester {

    public static void main(String[] args) throws IOException {

        ChallengeServer challengeServer = new ChallengeServer();
        for (int i = 0; i < 3; i ++ ) {
            System.out.println(challengeServer.getNextChallengeDescription().getInitialCode());
        }


    }
}

