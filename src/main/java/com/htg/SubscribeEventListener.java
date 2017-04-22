package com.htg;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * Created by Morten on 22/04/2017.
 */
@Component
public class SubscribeEventListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof SessionSubscribeEvent) {
            SessionSubscribeEvent sessionSubscribeEvent = (SessionSubscribeEvent) event;
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
            System.out.println("Socket request from: " + headerAccessor.getSessionAttributes().get("sessionID"));
        }
    }
}