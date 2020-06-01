package com.dhbwstudent.brainstormbe.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Comment {
    @Setter(AccessLevel.NONE)
    private static int idNo = 0;
    @Setter(AccessLevel.NONE)
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
