package com.dhbwstudent.brainstormbe.wss.main;

import com.dhbwstudent.brainstormbe.api.main.MainService;
import com.dhbwstudent.brainstormbe.wss.main.model.WebSocketMessage;
import com.dhbwstudent.brainstormbe.wss.main.model.WebSocketResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
public class WebSocketController {

    @Autowired
    private MainService mainService;

    @MessageMapping("/subscribe")
    @SendToUser("/topic/room")
    public WebSocketResponse subscribe(WebSocketMessage webSocketMessage, Principal principal) {
        try {
            log.info("received message: {} from {}", webSocketMessage, principal.getName());
            if (mainService.addUserName(principal.getName(), webSocketMessage.getRoomId())) {
                return new WebSocketResponse("Successfully subscribed!", "success");
            } else {
                return new WebSocketResponse("Room not found", "error");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new WebSocketResponse("An error occured: " + e.getMessage(), "error");
        }
    }

    @MessageMapping("/unsubscribe")
    @SendToUser("/topic/room")
    public WebSocketResponse unsubscribe(WebSocketMessage webSocketMessage, Principal principal) {
        try {
            log.info("received unsubcribe: {} from {}", webSocketMessage, principal.getName());
            if (mainService.unSubscribe(principal.getName(), webSocketMessage.getRoomId())) {
                return new WebSocketResponse("Successfully unsubscribed!", "success");
            } else {
                return new WebSocketResponse("Room not found", "error");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new WebSocketResponse("An error occured: " + e.getMessage(), "error");
        }
    }
}
