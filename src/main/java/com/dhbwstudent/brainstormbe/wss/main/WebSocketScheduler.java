package com.dhbwstudent.brainstormbe.wss.main;

import com.dhbwstudent.brainstormbe.api.main.MainService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public class WebSocketScheduler {
    @Autowired
    private MainService mainService;

//    @Scheduled(fixedDelayString = "3000", initialDelayString = "0")
//    public void schedulingTask(){
//        mainService.sendMessages();
//    }

}
