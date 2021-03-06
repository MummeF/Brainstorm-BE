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
    private String description;
    private State state;
    private String moderatorId;
    @Getter(AccessLevel.NONE)
    private String moderatorPassword;
    private boolean isPublic;
    @Getter(AccessLevel.NONE)
    private String password;
    private ArrayList<Contribution> contributions = new ArrayList<>();

    public void addContribution(List<Contribution> contributions) {
        this.contributions.addAll(contributions);
    }

    public void addContribution(Contribution contribution) {
        this.contributions.add(contribution);
    }

    public boolean hasPassword() {
        return this.password != null && !this.password.equals("");
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

    public boolean addContributionSubject(long contributionId, String subject) {
        for (int i = 0; i < contributions.size(); i++) {
            if (contributionId == contributions.get(i).getId()) {
                contributions.get(i).setSubject(subject);
                return true;
            }
        }
        return false;
    }

    public boolean updateContribution(long contributionId, String content, String subject) {
        for (int i = 0; i < contributions.size(); i++) {
            if (contributionId == contributions.get(i).getId()) {
                if (content != null) {
                    contributions.get(i).setContent(content);
                }
                if (subject != null) {
                    contributions.get(i).setSubject(subject);
                }
                return true;
            }
        }
        return false;
    }

    public boolean validatePassword(String password) {
        return password.equals(this.password);
    }

    public boolean validateModeratorId(String moderatorId) {
        return moderatorId.equals(this.moderatorId);
    }

    public boolean validateModeratorPassword(String password) {
        return moderatorPassword.equals(password);
    }


}
