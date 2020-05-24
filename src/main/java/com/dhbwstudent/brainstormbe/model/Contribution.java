package com.dhbwstudent.brainstormbe.model;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contribution {
    @Setter(AccessLevel.NONE)
    private static long contributionIdNr = 0;
    String content;
    String subject; //Für Gruppierung
    private int reputation;
    private List<Comment> comments;
    long id;

    public Contribution(String aContent) {
        this.content = aContent;
        id = generateId();
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public static long generateId(){
        return contributionIdNr++;
    }
}
