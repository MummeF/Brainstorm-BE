package com.dhbwstudent.brainstormbe.model;

public class Input {
    private static long id = 0;
    String content;
    long inputId; //Eindeutige ID aller Inputs
    public Input (String aContent) {
        this.content = aContent;
        inputId = id++;
    }

    public String getContent() {
        return content;
    }

    public long getInputId() {
        return inputId;
    }
}
