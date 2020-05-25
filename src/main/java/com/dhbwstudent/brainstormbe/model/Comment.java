package com.dhbwstudent.brainstormbe.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comment {
    private static int idNo = 0;
    private int id;
    private String content;
    private int reputation;

    public Comment(String content) {
        this.content = content;
        this.id = idNo++;
        this.reputation = 0;
    }

    public void voteUp() {
        reputation++;
    }

    public void voteDown() {
        reputation--;
    }
}
