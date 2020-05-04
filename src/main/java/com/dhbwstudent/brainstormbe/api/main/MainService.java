package com.dhbwstudent.brainstormbe.api.main;


import com.dhbwstudent.brainstormbe.model.RoomModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class MainService {

    private HashMap<Long, RoomModel> idToRoom = new HashMap<>();

    public MainService() {

    }

    public Long generateRandomSessionId() {
        long roomId = (long) (Math.random() * 899999) + 100000;
        idToRoom.put(roomId,
                RoomModel.builder()
                        .id(roomId)
                        .topic("")
                        .build());
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
            idToRoom.remove(roomModel.getId());
            idToRoom.put(roomModel.getId(), roomModel);
            return true;
        }
        return false;
    }
}
