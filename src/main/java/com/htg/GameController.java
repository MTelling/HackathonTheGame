package com.htg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @Autowired
    private ServerState state;

    @MessageMapping("/game")
    @SendToUser("/queue/game")
    public GameResponse gameResponse(GameRequest gameRequest, SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws Exception {
        String sessionID = simpMessageHeaderAccessor.getSessionAttributes().get("sessionID").toString();
        Application.logger.info(sessionID + " requested new game.");
        return new GameResponse(state.getCurrentChallengeDescription());
    }

}
