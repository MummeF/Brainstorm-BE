package com.dhbwstudent.brainstormbe.main;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

    @Service
    public class MainService {

        private List<Long> sessionIds = new ArrayList<>();

        public MainService() {

        }

        public long generateRandomSessionId() {
            long sessionId = (long) (Math.random() * 999999);
            sessionIds.add(sessionId);
            return sessionId;
        }

        public boolean validateSessionId(long sessionId) {
            return sessionIds.stream().anyMatch(id -> id == sessionId);
        }
}
