package com.htg;

import org.apache.catalina.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;


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

        String response = "Yo code is shit";

        announceWin(state.getUser(sessionID).getName());

        return new PMResponse(response);
    }


    public void announceWin(String winner) {
        System.out.println("Announcing new winner");
        simpMessagingTemplate.convertAndSend("/topic/news", new NewsResponse("Winner is: " + winner));
    }


}
