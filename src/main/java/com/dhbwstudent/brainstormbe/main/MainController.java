package com.dhbwstudent.brainstormbe.main;

import com.dhbwstudent.brainstormbe.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "http://brainstorm-dhbw.herokuapps.com"})
@RestController
public class MainController {
    @Autowired
    private MainService mainService;


    @RequestMapping(path = "/isAlive",
            method = RequestMethod.GET)
    public ResponseEntity<String> isAlive() {
        return ResponseEntity.ok("Hello, I am alive!")
                ;
    }

    @RequestMapping(path = "/",
            method = RequestMethod.GET)
    public ResponseEntity<String> Startseite() {
        return ResponseEntity.ok("Hallo, ist da die krosse Krabbe? Nein, hier ist Patrick!");
    }

    @RequestMapping(path = "/rndRoomId",
            method = RequestMethod.GET)
    public ResponseEntity<Room> rndRoomId() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(mainService.generateRandomSessionId());
    }

    @RequestMapping(path = "/validateRoomId",
            method = RequestMethod.GET)
    public ResponseEntity<Boolean> validateRoomId(@RequestParam long sessionId) {
        return ResponseEntity.ok(mainService.validateSessionId(sessionId));
    }

}
