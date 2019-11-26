package uk.co.craigcodes.devbadge.model.github;

import lombok.Data;

@Data
public class ContributionDetails {
    private String repoName;

    private int commitCount;
}
