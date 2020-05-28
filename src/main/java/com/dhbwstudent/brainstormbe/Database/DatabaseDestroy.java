package com.dhbwstudent.brainstormbe.Database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Slf4j
public class DatabaseDestroy {
    @PreDestroy
    public void destroy(){
        log.info("closing db connection");
        DB.closeConnection();
    }
}
