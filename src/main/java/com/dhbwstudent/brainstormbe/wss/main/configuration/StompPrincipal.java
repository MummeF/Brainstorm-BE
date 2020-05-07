package com.dhbwstudent.brainstormbe.wss.main.configuration;

import lombok.AllArgsConstructor;

import java.security.Principal;
import java.util.UUID;

@AllArgsConstructor
public class StompPrincipal implements Principal {

    private String name;

    @Override
    public String getName() {
        return this.name;
    }
}
