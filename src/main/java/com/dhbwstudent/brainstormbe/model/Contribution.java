package com.dhbwstudent.brainstormbe.model;


import lombok.*;

import java.util.ArrayList;
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
        reputation = 0;
        comments = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public boolean voteCommentUp(long id){
        for (int i = 0; i < comments.size(); i++) {
            if(comments.get(i).getId() == id){
                comments.get(i).voteUp();
                return true;
            }
        }
        return false;
    }
    public boolean voteCommentDown(long id){
        for (int i = 0; i < comments.size(); i++) {
            if(comments.get(i).getId() == id){
                comments.get(i).voteDown();
                return true;
            }
        }
        return false;
    }

    public void voteUp() {
        reputation++;
    }

    public void voteDown() {
        reputation--;
    }

    public static long generateId() {
        return contributionIdNr++;
    }
}
