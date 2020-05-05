package com.dhbwstudent.brainstormbe.main;


import com.dhbwstudent.brainstormbe.room.Room;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

    @Service
    public class MainService {

        //private List<Room> rooms = new ArrayList<>(); -> Liste ist nun in Klasse Room!

        public MainService() {

        }

        public long createRoom(String aSubject) {
            Room newRoom = new Room(aSubject);
            //rooms.add(newRoom); -> Liste ist nun in Klasse Room!
            return newRoom.getRoomId();
        }

        public boolean validateRoomId(long roomId) {
            return Room.roomIdExists(roomId);
        }
}
