package com.dhbwstudent.brainstormbe.api.main;


import com.dhbwstudent.brainstormbe.model.Room;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

    @Service
    public class MainService {

        private List<Long> sessionIds = new ArrayList<>();

        public MainService() {

        }

        public Room generateRandomSessionId() {
            long roomId = (long) (Math.random() * 999999);
            sessionIds.add(roomId);
            return Room.builder()
                    .id(roomId)
                    .build();
        }

        public boolean validateSessionId(long sessionId) {
            return sessionIds.stream().anyMatch(id -> id == sessionId);
        }
}
