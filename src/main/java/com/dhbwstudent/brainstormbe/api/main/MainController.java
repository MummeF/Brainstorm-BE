package com.dhbwstudent.brainstormbe.api.main;

import com.dhbwstudent.brainstormbe.model.Contribution;
import com.dhbwstudent.brainstormbe.model.RoomModel;
import com.dhbwstudent.brainstormbe.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "https://brainstorm-dhbw.herokuapp.com", "http://brainstorm-dhbw.herokuapp.com"})
@RestController
@RequestMapping("/api")
public class MainController {
    @Autowired
    private MainService mainService;


    @RequestMapping(path = "/isAlive",
            method = RequestMethod.GET)
    public ResponseEntity<String> isAlive() {
        return ResponseEntity.ok("Hello, I am alive!");
    }

    @RequestMapping(path = "/",
            method = RequestMethod.GET)
    public ResponseEntity<String> Startseite() {
        return ResponseEntity.ok("Wie man sieht, sieht man nichts");
    }

    @RequestMapping(path = "/createRoom",
            method = RequestMethod.GET)
    public ResponseEntity<Long> createRoom(@RequestParam(required = false) String topic, @RequestParam boolean isPublic,
                                           @RequestParam(required = false) String password, @RequestParam String moderatorId) {
        return ResponseEntity.ok(mainService.createRoom(topic, isPublic, password, moderatorId)); //TODO: IsPublic, password, moderatorId in FE mitaufnehmen!
    }

    @RequestMapping(path = "/setPassword",
            method = RequestMethod.POST)
    //TODO: Endpunkt neu, in Frontend reinbringen
    public ResponseEntity<Boolean> setPassword(@RequestParam long roomId, @RequestBody String password) {
        return ResponseEntity.ok(mainService.setPassword(roomId, password));
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

    //TODO: Endpunkt neu, in Frontend reinbringen
    @RequestMapping(path = "/setRoomState",
            method = RequestMethod.POST)
    public ResponseEntity<String> setRoomState(@RequestParam long roomId,  @RequestParam State state) {
        if (this.mainService.setRoomState(roomId, state)) {
            return ResponseEntity.ok("Successfully set Room '" + roomId + "' to State '" + state.value() + "'");

        }
        return ResponseEntity.badRequest().body("Could not find roomModel with id '" + roomId + "'");
    }

    @RequestMapping(path = "/addContribution",
            method = RequestMethod.POST)
    public ResponseEntity<String> addContribution(@RequestBody Contribution contribution, @RequestParam long roomId) {
        if (this.mainService.addContribution(contribution, roomId)) {
            return ResponseEntity.ok("Successfully added Contribution to Room with id '" + roomId + "'");
        }
        return ResponseEntity.badRequest().body("Could not find roomModel with id '" + roomId + "'");
    }

    @RequestMapping(path = "/deleteContribution",
            method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteContribution(@RequestParam long roomId, @RequestParam long contributionId) {
        if (mainService.deleteContribution(roomId, contributionId)) {
            return ResponseEntity.ok("Successfully deletet Contribution!");
        }
        return ResponseEntity.badRequest().body("Unable to find room or the belonging contribution");

    }

    @RequestMapping(path = "/updateContribution",
            method = RequestMethod.PUT)
    public ResponseEntity<String> updateContribution(@RequestParam long roomId, @RequestParam long contributionId,
                                                     @RequestParam String content, @RequestParam(required = false) String subject) {
        if (mainService.updateContribution(roomId, contributionId, content, subject)) {
            return ResponseEntity.ok("Successfully updated Contribution!");
        }
        return ResponseEntity.badRequest().body("Unable to find room or the belonging contribution");
    }

    @RequestMapping(path = "/deleteRoom",
            method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteRoom(@RequestParam long roomId) {
        if (mainService.deleteRoom(roomId)) {
            return ResponseEntity.ok("Successfully deleted room!");
        }
        return ResponseEntity.badRequest().body("Unable to find room with Id '+" + roomId + "'");
    }

    @RequestMapping(path = "/getContribution",
            method = RequestMethod.GET)

    public ResponseEntity<Contribution> getContribution(@RequestParam long roomId, @RequestParam long contributionId) {
        Contribution contribution = mainService.getContribution(roomId, contributionId);
        if (contribution != null) {
            return ResponseEntity.ok(contribution);
        }
        return ResponseEntity.badRequest().body(null);
    }
}
