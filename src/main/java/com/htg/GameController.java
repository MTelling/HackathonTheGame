package com.htg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * Created by Morten on 22/04/2017.
 */
@Controller
public class GameController {

    @MessageMapping("/game")
    @SendTo("/topic/game")
    public GameResponse gameResponse(GameRequest gameRequest, SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws Exception {
        String sessionID = simpMessageHeaderAccessor.getSessionAttributes().get("sessionID").toString();


        return new GameResponse(sessionID);
    }

}
