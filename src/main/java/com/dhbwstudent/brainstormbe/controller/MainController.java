package com.dhbwstudent.brainstormbe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @RequestMapping(path = "/isAlive",
            method = RequestMethod.GET)
    public String isAlive() {
        return "Hello, I am alive!";
    }

}
