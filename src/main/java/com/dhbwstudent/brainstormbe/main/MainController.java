package com.dhbwstudent.brainstormbe.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
