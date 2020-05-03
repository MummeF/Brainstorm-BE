package com.dhbwstudent.brainstormbe.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "http://brainstorm-dhbw.herokuapps.com"}, allowedHeaders = "*")
@RestController
public class MainController {
    @Autowired
    private MainService mainService;


    @RequestMapping(path = "/isAlive",
            method = RequestMethod.GET)
    public String isAlive() {
        return "Hello, I am alive!";
    }

    @RequestMapping(path = "/",
            method = RequestMethod.GET)
    public String Startseite() {
        return "Hallo, ist da die krosse Krabbe? Nein, hier ist Patrick!";
    }

    @RequestMapping(path = "/rndSessionId",
            method = RequestMethod.GET)
    public long rndSessionId() {
        return mainService.generateRandomSessionId();
    }

    @RequestMapping(path = "/validateSessionId",
            method = RequestMethod.GET)
    public boolean validateSessionId(@RequestParam long sessionId){
        return mainService.validateSessionId(sessionId);
    }

}
