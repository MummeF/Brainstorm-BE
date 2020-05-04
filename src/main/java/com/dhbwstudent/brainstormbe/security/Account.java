package com.dhbwstudent.brainstormbe.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
    private String name;
    private String password;
    private Role role;
}
