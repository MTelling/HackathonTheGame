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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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


        String  code = pmRequest.getCode(),
                fileName = sessionID + ".java",
                path = Paths.get("").toAbsolutePath().toString() + "\\Testing\\";

        // Write code to java file
        try(  PrintWriter out = new PrintWriter( "Testing/" + fileName ) ){ out.println( code ); }

        // Compile code to class file
        ProcessBuilder builder = new ProcessBuilder();
        builder.command( "javac", path + fileName );
        Process proc = builder.start();
        BufferedReader output = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        proc.waitFor();

        // If code fails compiling, return some sort of error for that
        if ( output.lines().toArray().length > 0 ) {
            System.out.println("ERROR COMPILING");
            return new PMResponse("ERROR COMPILING");
        }

        // Code could compile, run tests and return results
        builder.command("jar", "");
        proc = builder.start();
        output = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        proc.waitFor();

        // Debug log
        System.out.println("Process completed");
        System.out.println(Arrays.toString(output.lines().toArray()));

        // Build result
        StringBuilder result = new StringBuilder();
        for(Object s : output.lines().toArray())
            result.append((String) s);

        // Check if all tests are completed
        boolean hasWon = result.toString().split(",")[0].split(" ")[1].equals("true");
        if ( hasWon )
            announceWin(state.getUser(sessionID).getName());

        return new PMResponse(result.toString());
    }


    public void announceWin(String winnerUsername) {
        System.out.println("Announcing new winner");
        state.goToNextChallenge();
        simpMessagingTemplate.convertAndSend("/topic/news", new NewsResponse("win", winnerUsername));
    }


}
