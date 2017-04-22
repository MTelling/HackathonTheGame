package com.htg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

/**
 * Created by Morten on 22/04/2017.
 */
@Controller
public class LoginController {

    @Autowired
    private ServerState serverState;


    @MessageMapping("/login")
    @SendToUser("/queue/login")
    public LoginResponse login(LoginRequest message, SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws Exception {
        String sessionID = simpMessageHeaderAccessor.getSessionAttributes().get("sessionID").toString();
        User user = new User(message.getUsername());
        if (serverState.addUser(sessionID, user)) {
            return new LoginResponse("success");
        } else {
            return new LoginResponse("exists");
        }
    }
}