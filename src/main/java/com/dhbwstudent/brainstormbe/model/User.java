package com.dhbwstudent.brainstormbe.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class User {
    private String name;
    private List<Long> subscribedRooms = new ArrayList<>();

    public User(String name){
        this.name = name;
    }

    public boolean anyRoomSubscribed(){
        return !subscribedRooms.isEmpty();
    }

    public void subscribe(long roomId){
        subscribedRooms.add(roomId);
    }

    public boolean unsubscribe(long roomId){
        return subscribedRooms.remove(roomId);
    }
}
