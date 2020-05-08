package com.dhbwstudent.brainstormbe.wss.main.configuration;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.security.Principal;

@AllArgsConstructor
@NoArgsConstructor
public class WebSocketPrincipal implements Principal {
    private String name;

    @Override
    public String getName() {
        return this.name;
    }
}
