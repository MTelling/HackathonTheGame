package com.htg;

import com.google.gson.Gson;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.nio.file.Paths;


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
        Object[][] compileResults;

        String  code = pmRequest.getCode(),
                testingPath = "Testing",
                compilePath = "",
                path = Paths.get("").toAbsolutePath().toString(),
                classNamePrefix = "P",
                className = classNamePrefix + sessionID,
                fileName = className + ".java";


        code = code.replace("public class Program", "public class " + className);

        // Write code to java file
        try( PrintWriter out = new PrintWriter( testingPath + "/" + fileName ) ) { out.println( code ); }


        // Compiles given code, returns if it failed compiling
        if ( run("javac", path  + "/" + testingPath + "/" + fileName)[1].length > 0 ) {
            System.out.println("COMPILING ERROR");
            return new PMResponse("COMPILING ERROR");
        }

        // Runs tests
        System.out.println(path + "Compiler.jar");
        compileResults = run("java", "-jar", path + "/Compiler.jar", state.getCurrentChallengeDescription().getFilename(), className);
        // If stuff fuck up, blame Tobias
        for (int i = 0; i < compileResults.length; i++) {
            System.out.println(compileResults[i].toString());
        }

        if ( compileResults[1].length > 0 ) {
            System.out.println("SERVER ERROR");
            return new PMResponse("SERVER ERROR");
        }

        // Build result
        StringBuilder result = new StringBuilder();
        for(Object s : compileResults[0])
            result.append((String)s);

        // Check if all tests are completed
        RunnerResult runnerResult = new Gson().fromJson(result.toString(), RunnerResult.class);
        if ( runnerResult.isSuccess() )
            announceWin(state.getUser(sessionID).getName());

        return new PMResponse(result.toString());
    }


    private Object[][] run(String... args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(args);
        Process proc = pb.start();
        BufferedReader errors = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        BufferedReader output = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        proc.waitFor();
        return new Object[][]{output.lines().toArray(), errors.lines().toArray()};
    }


    public void announceWin(String winnerUsername) {
        System.out.println("Announcing new winner");
        state.goToNextChallenge();
        simpMessagingTemplate.convertAndSend("/topic/news", new NewsResponse("win", winnerUsername));
    }


}
