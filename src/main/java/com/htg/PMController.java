package com.htg;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;



@Controller
public class PMController {


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ServerState state;

    private final Object winLock = new Object();

    @MessageMapping("/pm")
    @SendToUser("/queue/pm")
    public PMResponse codeCheck(PMRequest pmRequest, SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws Exception {
        String sessionID = simpMessageHeaderAccessor.getSessionAttributes().get("sessionID").toString();
        String challengeID = state.getCurrentChallengeDescription().getFilename();

        if (sessionID == null || state.getUser(sessionID) == null) {
            Application.logger.info("Someone submitted code without being logged in!");
            return new PMResponse("You need to be logged in!!! Please refresh the page.");
        }

        String code = pmRequest.getCode();
        String challenge = state.getCurrentChallengeDescription().getFilename();

        String resultFromCall = "";
        try {
            resultFromCall = callRunManager(code, sessionID, challenge);
        } catch (Exception e) {
            Application.logger.severe("Error connecting to code runner. Service down?");
            return new PMResponse("Error connecting to the code runner. Please try again!");
        }

        // Check if all tests are completed
        try {
            RunnerResult runnerResult = new Gson().fromJson(resultFromCall, RunnerResult.class);

            if ( runnerResult.isSuccess()) {

                if (challengeID.equals(state.getCurrentChallengeDescription().getFilename())) {

                    // This makes sure that another player cannot submit a success if someone else just won.
                    // This is quite important because otherwise a player can win the next challenge, with the result
                    // from the previous challenge.
                    synchronized (winLock) {
                        if (challengeID.equals(state.getCurrentChallengeDescription().getFilename())) {
                            state.goToNextChallenge();
                            state.updateUserScore(sessionID, 1);
                            announceWin(state.getUser(sessionID).getName());

                            return new PMResponse(createOutputString(runnerResult));
                        }
                    }

                } else {
                    return new PMResponse("You succeeded, but someone beat you to it!");
                }
            }

            if (!challengeID.equals(state.getCurrentChallengeDescription().getFilename())) {
                return new PMResponse("Sorry, your code didn't succeed but another player's did.");
            }

            return new PMResponse(createOutputString(runnerResult));

        } catch (JsonParseException e) {

            return new PMResponse(resultFromCall);

        }



    }


    private String callRunManager(String code, String sessionID, String challenge) throws Exception {
        RunRequest runRequest = new RunRequest(sessionID, code, challenge);

        String url = Application.urlToRunner;

        HttpResponse jsonResponse = Unirest.post(url)
                .header("accept", "application/json")
                .body(new Gson().toJson(runRequest)).asString();

        return jsonResponse.getBody().toString();
    }

    private String createOutputString(RunnerResult runnerResult) {
        final String NEW_LINE = "\n";
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


    public void announceWin(String winnerUsername) {
        Application.logger.info(winnerUsername + " won the current game.");
        simpMessagingTemplate.convertAndSend("/topic/news", new NewsResponse("win", winnerUsername, state.getLeaderboard()));
    }




}
