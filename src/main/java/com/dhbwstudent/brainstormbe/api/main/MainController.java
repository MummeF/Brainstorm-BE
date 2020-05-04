package com.dhbwstudent.brainstormbe.api.main;

import com.dhbwstudent.brainstormbe.model.RoomModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "https://brainstorm-dhbw.herokuapp.com", "http://brainstorm-dhbw.herokuapp.com"})
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
    public ResponseEntity<Long> rndRoomId() {
        return ResponseEntity.ok(mainService.generateRandomSessionId());
    }

    @RequestMapping(path = "/validateRoomId",
            method = RequestMethod.GET)
    public ResponseEntity<Boolean> validateRoomId(@RequestParam long roomId) {
        return ResponseEntity.ok(mainService.validateRoomId(roomId));
    }

    @RequestMapping(path = "/getRoom",
            method = RequestMethod.GET)
    public ResponseEntity<RoomModel> getRoom(@RequestParam long roomId) {
        if (mainService.validateRoomId(roomId)) {
            return ResponseEntity.ok(mainService.getRoom(roomId));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @RequestMapping(path = "/updateRoom",
            method = RequestMethod.POST)
    public ResponseEntity<String> updateRoom(@RequestBody RoomModel roomModel) {
        if (this.mainService.validateRoomId(roomModel.getId())) {
            if (this.mainService.updateRoom(roomModel)) {
                return ResponseEntity.ok("Successfully update roomModel with id '" + roomModel.getId() + "'");
            }

        }
        return ResponseEntity.badRequest().body("Could not find roomModel with id '" + roomModel.getId() + "'");
    }

}
