package com.htg;

import org.apache.catalina.Server;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


@Controller
public class PMController {


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ServerState state;

    @MessageMapping("/pm")
    @SendToUser("/queue/pm")
    public PMResponse codeCheck(PMRequest pmRequest, SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws Exception {
        String sessionID = simpMessageHeaderAccessor.getSessionAttributes().get("sessionID").toString();

        System.out.println("In the pmcontroller");
        String[][] compileResults;

        String  code = pmRequest.getCode(),
                fileName = sessionID + ".java",
                testingPath = "Testing",
                compilePath = "",
                path = Paths.get("").toAbsolutePath().toString();

        // Write code to java file
        try(  PrintWriter out = new PrintWriter( testingPath + "/" + fileName ) ){ out.println( code ); }

        // Compiles given code, returns if it failed compiling
        if ( compile("javac", path  + "/" + testingPath + "/" + fileName)[1].length > 0 ) {
            System.out.println("COMPILING ERROR");
            return new PMResponse("COMPILING ERROR");
        }
        // Runs tests
        compileResults = compile("java", "-jar", path + "/" + compilePath + "/compiler.jar");
        // If stuff fuck up, blame Tobias
        if ( compileResults[1].length > 0 ) {
            System.out.println("SERVER ERROR");
            return new PMResponse("SERVER ERROR");
        }

        // Build result
        StringBuilder result = new StringBuilder();
        for(String s : compileResults[0])
            result.append(s);

        // Check if all tests are completed
        boolean hasWon = result.toString().split(",")[0].split(" ")[1].equals("true");
        if ( hasWon )
            announceWin(state.getUser(sessionID).getName());

        return new PMResponse(result.toString());
    }


    private String[][] compile(String... args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(args);
        Process proc = pb.start();
        BufferedReader errors = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        BufferedReader output = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        proc.waitFor();
        return new String[][]{(String[])output.lines().toArray(), (String[])errors.lines().toArray()};
    }


    public void announceWin(String winnerUsername) {
        System.out.println("Announcing new winner");
        state.goToNextChallenge();
        simpMessagingTemplate.convertAndSend("/topic/news", new NewsResponse("win", winnerUsername));
    }


}
