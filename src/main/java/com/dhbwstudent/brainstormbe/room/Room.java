package com.dhbwstudent.brainstormbe.room;

import com.dhbwstudent.brainstormbe.input.Input;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private static List<Room> rooms = new ArrayList<>();
    private long roomId;
    private String subject;
    private ArrayList<Input> inputs;

    public Room(String aSubject) {
        do {
            roomId = (long) (Math.random() * 999999);
        } while(roomIdExists(roomId));
        this.subject = aSubject;
        rooms.add(this);
        inputs = new ArrayList<>();
    }

    public void addInput(String aContent) {
        inputs.add(new Input(aContent));
    }

    public boolean removeInputById(long aInputId) {
        return inputs.removeIf(
                id -> id.getInputId() == aInputId
        );
    }

    public String getSubject() {
        return subject;
    }

    public long getRoomId() {
        return roomId;
    }

    public static boolean roomIdExists(long aRoomId) {
        return rooms.stream().anyMatch(room -> room.getRoomId() == aRoomId);
    }
}
