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
        return "Was ist gelb, hat einen Arm, und kann nicht schwimmen?! EIN BAGGER";
    }

    @RequestMapping(path = "/createRoom",
            method = RequestMethod.GET)
    public long createRoom() {
        return mainService.createRoom("Thema!!");
    } //TODO User muss Thema eingeben und der URL übergeben!!

    @RequestMapping(path = "/validateRoomId",
            method = RequestMethod.GET)
    public boolean validateRoomId(@RequestParam long roomId){
        return mainService.validateRoomId(roomId);
    }

}
