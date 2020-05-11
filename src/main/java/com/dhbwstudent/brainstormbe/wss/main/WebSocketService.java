package com.dhbwstudent.brainstormbe.wss.main;

import com.dhbwstudent.brainstormbe.model.Room;
import com.dhbwstudent.brainstormbe.model.User;
import com.dhbwstudent.brainstormbe.wss.main.model.WebSocketResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebSocketService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @Getter(AccessLevel.PUBLIC)
    private static List<User> users = new ArrayList<>();

    public static void addUser(User user) {
        users.add(user);
    }

    public static boolean removeUser(User user){
        return users.remove(user);
    }

    public void sendToUser(String username, Room room) {
        try {
            sendToUser(username, new WebSocketResponse(objectMapper.writeValueAsString(room), "data"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendToUser(String userName, WebSocketResponse response) {
        try {
            simpMessagingTemplate.convertAndSendToUser(userName, "/topic/room",
                    objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendAlive() {
        sendToAll(new WebSocketResponse("Still alive!", "isAlive"));
    }

    public void sendToAll(WebSocketResponse response) {
        for (User user : users) {
            sendToUser(user.getName(), response);
        }
    }
}
