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

    @MessageMapping("/login")
    @SendToUser("/queue/login")
    public LoginResponse login(LoginRequest message, SimpMessageHeaderAccessor simpMessageHeaderAccessor) throws Exception {
        String sessionID = simpMessageHeaderAccessor.getSessionAttributes().get("sessionID").toString();
        System.out.println("user is in logincontroller");
        System.out.println(message.getUsername());
        return new LoginResponse("success!");
    }
}