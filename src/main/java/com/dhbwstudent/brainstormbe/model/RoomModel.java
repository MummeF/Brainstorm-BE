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

    public void addContribution(List<Contribution> contributions) {
        this.contributions.addAll(contributions);
    }

    public void addContribution(Contribution contribution) {
        this.contributions.add(contribution);
    }

    public boolean removeContribution(long contributionId) {
        for (int i = 0; i < contributions.size(); i++) {
            if (contributionId == contributions.get(i).getId()) {
                contributions.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean updateContribution(long contributionId, String content) {
        for (int i = 0; i < contributions.size(); i++) {
            if (contributionId == contributions.get(i).getId()) {
                contributions.get(i).setContent(content);
                return true;
            }
        }
        return false;
    }
}
