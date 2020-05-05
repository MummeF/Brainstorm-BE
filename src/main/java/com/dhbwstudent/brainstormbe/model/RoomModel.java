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
    private ArrayList<Input> inputs = new ArrayList<>();

    public void addInput(List<Input> inputs){
        this.inputs.addAll(inputs);
    }
    public void addInput(Input input){
        this.inputs.add(input);
    }
}
