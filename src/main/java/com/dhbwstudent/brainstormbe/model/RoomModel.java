package com.dhbwstudent.brainstormbe.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomModel {
    @Setter(AccessLevel.NONE)
    private long id;
    private String topic;
    private ArrayList<Contribution> contributions = new ArrayList<>();
    public void addContribution(List<Contribution> contributions){
        this.contributions.addAll(contributions);
    }
    public void addContribution(Contribution contribution){
        this.contributions.add(contribution);
    }
}
