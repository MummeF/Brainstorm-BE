package com.dhbwstudent.brainstormbe.security;

public enum Role {

    User("USER"), Admin("ADMIN");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public String value(){
        return this.value;
    }
}
