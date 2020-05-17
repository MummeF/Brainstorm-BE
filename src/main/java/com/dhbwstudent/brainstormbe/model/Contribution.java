package com.dhbwstudent.brainstormbe.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contribution {
    private static long contributionIdNr = 0;
    String content;
    String subject; //Für Gruppierung
    long id;
    public Contribution(String aContent) {
        this.content = aContent;
        id = generateId();
    }

    public static long generateId(){
        return contributionIdNr++;
    }
}
