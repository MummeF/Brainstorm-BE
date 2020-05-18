package com.dhbwstudent.brainstormbe.api.main;


import com.dhbwstudent.brainstormbe.model.Contribution;
import com.dhbwstudent.brainstormbe.model.RoomModel;
import com.dhbwstudent.brainstormbe.model.State;
import com.dhbwstudent.brainstormbe.model.User;
import com.dhbwstudent.brainstormbe.wss.main.WebSocketService;
import com.dhbwstudent.brainstormbe.wss.main.model.WebSocketResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MainService {

    private static HashMap<Long, RoomModel> idToRoom = new HashMap<>();


    @Autowired
    private WebSocketService webSocketService;

    public Long createRoom(String topic, boolean isPublic, String password, String moderatorId) {
        long roomId;
        do {
            roomId = (long) (Math.random() * 899999) + 100000;
        } while (validateRoomId(roomId));

        idToRoom.put(roomId,
                RoomModel.builder()
                        .id(roomId)
                        .topic(topic != null ? topic : "")
                        .contributions(new ArrayList<>())
                        .isPublic(isPublic)
                        .password(password != null ? password : "")
                        .moderatorId(moderatorId)
                        .build());
        this.updateUser();
        return roomId;
    }

    public boolean setPassword(long roomId, String password) {
        if (validateRoomId(roomId)) {
            getRoom(roomId).setPassword(password != null ? password : "");
            return true;
        }
        log.warn("Setting Password failed, given RoomID doesn't exist");
        return false;
    }

    public boolean setRoomState(long roomId, State state) {
        if (validateRoomId(roomId)) {
            getRoom(roomId).setState(state);
            return true;
        }
        log.warn("Setting State failed, given RoomID doesn't exist");
        return false;
    }

    public boolean validateRoomId(long roomId) {
        return idToRoom.containsKey(roomId);
    }

    public static RoomModel getRoom(long roomId) { //Da die HashMap auch static ist, spricht nix dagegen diese Methode ebenfalls static zu dekl.
        return idToRoom.get(roomId);
    }

    public boolean updateRoom(RoomModel roomModel) {
        if (validateRoomId(roomModel.getId())) {
            RoomModel removed = idToRoom.remove(roomModel.getId());
            roomModel.setContributions(removed.getContributions());
            idToRoom.put(roomModel.getId(), roomModel);
            this.updateUser();
            return true;
        }
        log.warn("Updating Room failed, given RoomID doesn't exist");
        return false;
    }

    public boolean addContribution(Contribution contribution, long roomId) {
        if (validateRoomId(roomId)) {
            getRoom(roomId)
                    .addContribution(new Contribution(contribution.getContent()));
            this.updateUser();
            return true;
        }
        log.warn("Adding Contribution failed, given RoomID doesn't exist");
        return false;
    }

    public boolean deleteContribution(long roomId, long contributionId) {
        if (validateRoomId(roomId)) {
            boolean res = getRoom(roomId).removeContribution(contributionId);
            this.updateUser();
            return res;
        }
        log.warn("Deleting Contribution failed, given RoomID doesn't exist");
        return false;
    }

    public boolean updateContribution(long roomId, long contributionId, String content, String subject) {
        if (validateRoomId(roomId)) {
            boolean res = getRoom(roomId).updateContribution(contributionId, content, subject != null ? subject : "");
            this.updateUser();
            return res;
        }
        log.warn("Updating Contribution failed, given RoomID doesn't exist");
        return false;
    }

    public Contribution getContribution(long roomId, long contributionId) {
        if (validateRoomId(roomId)) {
            return getRoom(roomId).getContributions().stream()
                    .filter(contribution -> contribution.getId() == contributionId)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                        if (list.isEmpty() || list.size() > 1) {
                            return null;
                        }
                        return list.get(0);
                    }));
        }
        log.warn("Retrieving Contribution failed, given RoomID doesn't exist");
        return null;
    }

    public boolean deleteRoom(long roomId) {
        if (validateRoomId(roomId)) {
            idToRoom.remove(roomId);
            informUserAboutDeletedRoom(roomId);
            updateUser();
            return true;
        }
        log.warn("Deleting Room failed, given RoomID doesn't exist");
        return false;
    }

    //Websocket
    public void updateUser() {
        WebSocketService.getUsers().forEach(user ->
                user.getSubscribedRooms().forEach(roomId -> {
                    webSocketService.sendToUser(user.getName(), getRoom(roomId));
                })
        );
    }

    public void informUserAboutDeletedRoom(long deletedRoomId) {
        for (User user : WebSocketService.getUsers()) {
            for (int i = 0; i < user.getSubscribedRooms().size(); i++) {
                long roomId = user.getSubscribedRooms().get(i);
                if (roomId == deletedRoomId) {
                    user.unsubscribe(deletedRoomId);
                    if (!user.anyRoomSubscribed()) {
                        WebSocketService.removeUser(user);
                    }
                    webSocketService.sendToUser(user.getName(), new WebSocketResponse("deleted room with id " + roomId, "delete"));
                }
            }
        }
    }

    public boolean addUserName(String userName, long roomId) {
        if (validateRoomId(roomId)) {
            boolean userExists = WebSocketService.getUsers().stream()
                    .anyMatch(user -> user.getName().equals(userName));
            if (!userExists) {
                WebSocketService.addUser(new User(userName));
            }
            WebSocketService.getUsers().stream()
                    .filter(user -> user.getName().equals(userName))
                    .forEach(user -> user.subscribe(roomId));
            this.updateUser();
            return true;
        } else {
            log.warn("Tried to subscribe for not existing room");
            return false;
        }
    }


    public RoomModel[] getRoomList() {
        RoomModel[] arr = new RoomModel[idToRoom.values().size()];
        arr = idToRoom.values().toArray(arr);
        return arr;
    }

    public boolean validatePassword(long roomId, String password) {
        if(validateRoomId(roomId)){
            return idToRoom.get(roomId).validatePassword(password);
        }
        log.warn("Given RoomId doesn't exist");
        return false;
    }

    public boolean hasPassword(long roomId) {
        if(validateRoomId(roomId)){
            return idToRoom.get(roomId).hasPassword();
        }
        log.warn("Given RoomId doesn't exist");
        return false;
    }
}
