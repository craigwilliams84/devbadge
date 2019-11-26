package uk.co.craigcodes.devbadge.service.badge.evaluator;

import org.springframework.stereotype.Component;
import uk.co.craigcodes.devbadge.model.github.ContributionDetails;

@Component
public class MaestroLevelEvaluator extends AccumulativeCommitEvaluator {

    private static final int COMMIT_THRESHOLD = 50;

    private static final String NAME_FORMAT = "Maestro - %s";

    private static final String DESCRIPTION_FORMAT = "Woah, 50 contributions to the %s respository...you know what you're doing!!";

    public MaestroLevelEvaluator() {
        super(COMMIT_THRESHOLD);
    }

    @Override
    String generateDescription(String user, ContributionDetails contributionDetails) {
        return String.format(DESCRIPTION_FORMAT, contributionDetails.getRepoName());
    }

    @Override
    String generateName(String user, ContributionDetails contributionDetails) {
        return String.format(NAME_FORMAT, contributionDetails.getRepoName());
    }
}
