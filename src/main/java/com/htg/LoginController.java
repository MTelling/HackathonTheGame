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


    @MessageMapping("/checkLogin")
    @SendToUser("/queue/checkLogin")
    public LoginResponse checklogin(LoginRequest message, SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws Exception {
        String sessionID = simpMessageHeaderAccessor.getSessionAttributes().get("sessionID").toString();
        User user = serverState.getUser(sessionID);
        if (user != null) {
            return new LoginResponse("alreadyIn", user.getName(), user.getScore());
        } else {
            return new LoginResponse("newUser", "", 0);
        }
    }

    @MessageMapping("/login")
    @SendToUser("/queue/login")
    public LoginResponse login(LoginRequest message, SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws Exception {
        String sessionID = simpMessageHeaderAccessor.getSessionAttributes().get("sessionID").toString();
        User user = new User(message.getUsername());
        if (serverState.addUser(sessionID, user)) {
            return new LoginResponse("success", user.getName(), user.getScore());
        } else {
            return new LoginResponse("exists", user.getName(), user.getScore());
        }
    }
}