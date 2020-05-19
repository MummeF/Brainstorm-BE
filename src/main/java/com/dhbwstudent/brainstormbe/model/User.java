package com.dhbwstudent.brainstormbe.model;

import com.dhbwstudent.brainstormbe.api.main.MainService;
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

    public void subscribe(long roomId) { //TODO: Methode töten, neue überladene Methode nutzen
        subscribedRooms.add(roomId);
    }

    public void subscribe(long roomId, String pw) {
        if (MainService.getRoom(roomId).validatePassword(pw))
            subscribedRooms.add(roomId);
        //else?? TODO: Muss der User informiert werden wenn er versucht einem PW-geschützten
        // Raum beizutreten, bzw. dass PW falsch ist?
    }

    public boolean unsubscribe(long roomId){
        return subscribedRooms.remove(roomId);
    }
}
