package com.htg;

import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;


@Controller
public class PMController {


    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ServerState state;

    @MessageMapping("/pm")
    @SendToUser("/queue/pm")
    public PMResponse codeCheck(PMRequest pmRequest, SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws Exception {
        String sessionID = simpMessageHeaderAccessor.getSessionAttributes().get("sessionID").toString();

        System.out.println("In the pmcontroller");


        String code = pmRequest.getCode();
        // TODO: compile code, do magic, test results

        ProcessBuilder builder = new ProcessBuilder();
        builder.command("");
        Process proc = builder.start();
        BufferedReader output = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        proc.waitFor();

        StringBuilder result = new StringBuilder();
        result.append(output.lines());
        boolean hasWon = result.toString().split(",")[0].split(" ")[1].equals("true");
        if(hasWon)
            announceWin(state.getUser(sessionID).getName());
        return new PMResponse(result.toString());
    }


    public void announceWin(String winner) {
        System.out.println("Announcing new winner");
        simpMessagingTemplate.convertAndSend("/topic/news", new NewsResponse("Winner is: " + winner));
    }


}
