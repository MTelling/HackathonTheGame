package com.htg;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.io.*;
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

        if (sessionID == null) return new PMResponse("NO USER IN!!");

        System.out.println("In the pmcontroller");
        String[] compileResults;
        String[] runtimeResults;

        String  code = pmRequest.getCode(),
                testingPath = "Testing",
                compilePath = "",
                path = Paths.get("").toAbsolutePath().toString(),
                classNamePrefix = "P",
                className = classNamePrefix + sessionID,
                fileName = className + ".java";

        code = "package Testing; " + code;
        code = code.replace("public class Program", "public class " + className);

        // Write code to java file
        try( PrintWriter out = new PrintWriter( testingPath + "/" + fileName ) ) { out.println( code ); }

        // Compiles given code, returns if it failed compiling

        compileResults = run("javac", path  + "/" + testingPath + "/" + fileName);
        System.out.println("Compile result: " + Arrays.toString(compileResults));
        if (compileResults[1].length() > 0) {
            return new PMResponse(compileResults[1]);
        }

        // Runs tests
        System.out.println(path + "Compiler.jar");
        runtimeResults = run("java", "-jar", path + "/Compiler.jar", state.getCurrentChallengeDescription().getFilename(), className);
        System.out.println("Run result: " + Arrays.toString(runtimeResults));

        // If there are any errors, show them.
        if ( runtimeResults[1].length() > 0 ) {
            return new PMResponse(runtimeResults[1]);
        }

        // Build result
        String output = runtimeResults[0];

        // Check if all tests are completed
        RunnerResult runnerResult = new Gson().fromJson(output, RunnerResult.class);
        if ( runnerResult.isSuccess() ) {
            System.out.println("session id is: " + sessionID);
            state.updateUserScore(sessionID, 1);
            announceWin(state.getUser(sessionID).getName());
        }

        return new PMResponse(createOutputString(runnerResult));
    }

    private String createOutputString(RunnerResult runnerResult) {
        final String NEW_LINE = ", ";
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Succeeded: ").append(runnerResult.isSuccess() ? "Yes": "No").append(NEW_LINE);
        stringBuilder.append("Passed Tests: ").append(runnerResult.getPassedTests()).append(NEW_LINE);

        if (runnerResult.getErrors().size() > 0) {
            stringBuilder.append("Tests: ").append(NEW_LINE);
            runnerResult.getErrors().forEach((error) -> {
                stringBuilder.append(error).append(NEW_LINE);
            });
        }

        if (runnerResult.getRuntimeErrors().size() > 0) {
            stringBuilder.append("Runtime Errors: ").append(NEW_LINE);
            runnerResult.getRuntimeErrors().forEach((error) -> {
                stringBuilder.append(error).append(NEW_LINE);
            });
        }

        return stringBuilder.toString();
    }


    private String[] run(String... args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(args);
        Process proc = pb.start();
        BufferedReader errors = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
        BufferedReader output = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        proc.waitFor();

        StringBuilder errorBuilder = new StringBuilder();
        StringBuilder outputBuilder = new StringBuilder();

        String currentLine = null;
        while ((currentLine = errors.readLine()) != null) {
            errorBuilder.append(currentLine);
        }

        while ((currentLine = output.readLine()) != null) {
            outputBuilder.append(currentLine);
        }

        return new String[]{outputBuilder.toString(), errorBuilder.toString()};
    }


    public void announceWin(String winnerUsername) {
        System.out.println("Announcing new winner");
        state.goToNextChallenge();
        simpMessagingTemplate.convertAndSend("/topic/news", new NewsResponse("win", winnerUsername, state.getLeaderboard()));
    }


}
