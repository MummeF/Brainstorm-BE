package com.dhbwstudent.brainstormbe.model;


public enum State {
    CREATE("CREATE"), EDIT("EDIT"), DONE("DONE");

    private String value;

    State(String value) {
        this.value = value;
    }

    public String value(){
        return this.value;
    }
}
