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

        String language = pmRequest.getLanguage(),
                code = pmRequest.getCode();

        switch(language){
            case "JavaScript":
                return handleJSCode(code, sessionID);
            case "Java":
                return handleJavaCode(code, sessionID);
            default:
                return handleJavaCode(code, sessionID);
        }
    }

    private static final String testingPath = "Testing/",
                                absolutePath = Paths.get("").toAbsolutePath().toString(),
                                classNamePrefix = "P";

    private PMResponse handleJavaCode(String code, String sessionId) throws IOException, InterruptedException {

        String  className = classNamePrefix + sessionId,
                fileName = className + ".java";


        code = "package Testing; " + code;
        code = code.replace("public class Program", "public class " + className);



        // Write code to java file
        try( PrintWriter out = new PrintWriter( testingPath + "/" + fileName ) ) { out.println( code ); }

        // Compiles given code, returns if it failed compiling

        String[] compileResults = run("javac", absolutePath + "/" + testingPath + fileName);
        System.out.println("Compile result: " + Arrays.toString(compileResults));
        if (compileResults[1].length() > 0) {
            return new PMResponse(compileResults[1]);
        }

        // Runs tests
        System.out.println(absolutePath + "Runner.jar");
        String[] runtimeResults = run("java", "-jar", absolutePath + "/Runner.jar", state.getCurrentChallengeDescription().getFilename(), className, "Java");
        System.out.println("Run result: " + Arrays.toString(runtimeResults));

        // If there are any errors, show them.
        if ( runtimeResults[1].length() > 0 ) {
            return new PMResponse(runtimeResults[1]);
        }

        // Build output
        String output = runtimeResults[0];
        return handleOutput(output, sessionId);
    }

    private PMResponse handleJSCode(String code, String sessionId) throws IOException, InterruptedException {
        String extension = ".js";
        try {
            try( PrintWriter out = new PrintWriter( testingPath + classNamePrefix + sessionId + extension ) ) { out.println( code ); }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] runtimeResults = run("java", "-jar", absolutePath + "/Runner.jar",
                state.getCurrentChallengeDescription().getFilename(),
                classNamePrefix + sessionId,
                "Javascript");
        System.out.println("Run result: " + Arrays.toString(runtimeResults));

        // If there are any errors, show them.
        if ( runtimeResults[1].length() > 0 ) {
            return new PMResponse(runtimeResults[1]);
        }
        // Build output
        String output = runtimeResults[0];
        return handleOutput(output, sessionId);
    }

    PMResponse handleOutput(String output, String sessionId){
        RunnerResult runnerResult = new Gson().fromJson(output, RunnerResult.class);
        if ( runnerResult.isSuccess() ) {
            System.out.println("session id is: " + sessionId);
            state.updateUserScore(sessionId, 1);
            announceWin(state.getUser(sessionId).getName());
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
