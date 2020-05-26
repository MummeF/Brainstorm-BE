package com.dhbwstudent.brainstormbe.api.room;

import com.dhbwstudent.brainstormbe.api.main.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "https://brainstorm-dhbw.herokuapp.com", "http://brainstorm-dhbw.herokuapp.com"})
@RestController
@RequestMapping("/api")
public class RoomController {
    @Autowired
    private MainService mainService;

    @RequestMapping(path = "/createRoom",
            method = RequestMethod.GET)
    public ResponseEntity<Long> createRoom(@RequestParam(required = false) String topic, @RequestParam boolean isPublic,
                                           @RequestParam String moderatorId, @RequestParam(required = false) String description) {
        return ResponseEntity.ok(mainService.createRoom(topic, isPublic, moderatorId, description)); //TODO: IsPublic, password, moderatorId in FE mitaufnehmen!
    }
}
