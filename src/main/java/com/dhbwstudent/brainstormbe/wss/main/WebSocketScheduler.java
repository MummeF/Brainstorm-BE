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
    private WebSocketService webSocketService;

    @Scheduled(fixedDelay = 50*1000, initialDelay = 1000)
    public void sendAlive(){
        webSocketService.sendAlive();
    }

}
