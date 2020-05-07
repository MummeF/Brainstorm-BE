package com.dhbwstudent.brainstormbe.wss.main;

import com.dhbwstudent.brainstormbe.api.main.MainService;
import com.dhbwstudent.brainstormbe.wss.main.model.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.Principal;

@Slf4j
@Controller
public class WebSocketController {

    @Autowired
    private MainService mainService;

    @MessageMapping("/subscribeForRoom")
    @SendToUser("/topic/room")
    public String subscribe(Subscription subscription, Principal principal) {
        log.info("Subscribed for ID {} from {}", subscription.getRoomId(), principal.getName());
        if(mainService.addUserName(principal.getName(), subscription.getRoomId())){

            return "Successfully subscribed!";
        }else{
            return "Room not found";
        }
    }
}
