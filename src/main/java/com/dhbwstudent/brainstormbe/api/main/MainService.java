package com.dhbwstudent.brainstormbe.api.main;


import com.dhbwstudent.brainstormbe.model.Contribution;
import com.dhbwstudent.brainstormbe.model.RoomModel;
import com.dhbwstudent.brainstormbe.model.User;
import com.dhbwstudent.brainstormbe.wss.main.model.WebSocketResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MainService {

    private static HashMap<Long, RoomModel> idToRoom = new HashMap<>();
    private static HashMap<String, Long> userToRoomId = new HashMap<>();
    private static List<User> users = new ArrayList<>();

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public MainService() {

    }

    public Long createRoom(String topic) {
        long roomId = (long) (Math.random() * 899999) + 100000;
        while (idToRoom.containsKey(roomId)) {
            roomId = (long) (Math.random() * 899999) + 100000;
        }
        idToRoom.put(roomId,
                RoomModel.builder()
                        .id(roomId)
                        .topic(topic != null ? topic : "")
                        .contributions(new ArrayList<>())
                        .build());
        this.updateUser();
        return roomId;
    }

    public boolean validateRoomId(long roomId) {
        return idToRoom.containsKey(roomId);
    }

    public RoomModel getRoom(long roomId) {
        return idToRoom.get(roomId);
    }

    public boolean updateRoom(RoomModel roomModel) {
        if (idToRoom.containsKey(roomModel.getId())) {
            RoomModel removed = idToRoom.remove(roomModel.getId());
            roomModel.setContributions(removed.getContributions());
            idToRoom.put(roomModel.getId(), roomModel);
            this.updateUser();
            return true;
        }
        return false;
    }

    public boolean addContribution(Contribution contribution, long roomId) {
        if (idToRoom.containsKey(roomId)) {
            idToRoom.get(roomId)
                    .addContribution(new Contribution(contribution.getContent()));
            this.updateUser();
            return true;
        }
        return false;
    }

    public boolean deleteContribution(long roomId, long contributionId) {
        if (idToRoom.containsKey(roomId)) {
            boolean res = idToRoom.get(roomId).removeContribution(contributionId);
            this.updateUser();
            return res;
        }
        return false;
    }

    public boolean updateContribution(long roomId, long contributionId, String content) {
        if (idToRoom.containsKey(roomId)) {
            boolean res = idToRoom.get(roomId).updateContribution(contributionId, content);
            this.updateUser();
            return res;
        }
        return false;
    }

    public Contribution getContribution(long roomId, long contributionId) {
        if (idToRoom.containsKey(roomId)) {
            return idToRoom.get(roomId).getContributions().stream()
                    .filter(contribution -> contribution.getId() == contributionId)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                        if (list.isEmpty() || list.size() > 1) {
                            return null;
                        }
                        return list.get(0);
                    }));
        }
        return null;
    }

    public boolean deleteRoom(long roomId) {
        if (idToRoom.containsKey(roomId)) {
            idToRoom.remove(roomId);
            informUserAboutDeletedRoom(roomId);
            updateUser();
            return true;
        }
        return false;
    }

    //Websocket
    public void updateUser() {
        users.forEach(user ->
                user.getSubscribedRooms().forEach(roomId -> {
                    sendToUser(user.getName(), idToRoom.get(roomId));
                })
        );
    }

    private void sendToUser(String username, RoomModel room) {
        try {
            sendToUser(username, new WebSocketResponse(objectMapper.writeValueAsString(room), "data"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void sendToUser(String userName, WebSocketResponse response) {
        try {
            simpMessagingTemplate.convertAndSendToUser(userName, "/topic/room",
                    objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void informUserAboutDeletedRoom(long deletedRoomId) {
        for (User user : users) {
            if(user.getSubscribedRooms() != null) {
                for (int i = 0; i < user.getSubscribedRooms().size(); i++) {
                    long roomId = user.getSubscribedRooms().get(i);
                    if (roomId == deletedRoomId) {
                        user.unsubscribe(deletedRoomId);
                        if (!user.anyRoomSubscribed()) {
                            users.remove(user);
                        }
                        sendToUser(user.getName(), new WebSocketResponse("deleted room with id " + roomId, "delete"));
                    }
                }
            }
        }
    }


    public boolean addUserName(String userName, long roomId) {
        if (idToRoom.containsKey(roomId)) {
            boolean userExists = users.stream()
                    .anyMatch(user -> user.getName().equals(userName));
            if (!userExists) {
                users.add(new User(userName));
            }
            users.stream()
                    .filter(user -> user.getName().equals(userName))
                    .forEach(user -> user.subscribe(roomId));
            this.updateUser();
            return true;
        } else {
            log.warn("Tried to subscribe for not existing room");
            return false;
        }
    }


}
