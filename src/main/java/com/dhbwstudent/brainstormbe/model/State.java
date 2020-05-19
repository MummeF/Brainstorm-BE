package com.dhbwstudent.brainstormbe.model;

//TODO: Enum neu, in FE implementieren
public enum State {
    Create("CREATE"), Edit("EDIT"), Done("DONE");

    private String value;

    State(String value) {
        this.value = value;
    }

    public String value(){
        return this.value;
    }
}
