package uk.co.craigcodes.devbadge.service.badge.evaluator;

import org.springframework.stereotype.Component;
import uk.co.craigcodes.devbadge.model.github.ContributionDetails;

@Component
public class MaestroLevelEvaluator extends AccumulativeCommitEvaluator {

    private static final int COMMIT_THRESHOLD = 50;

    private static final String NAME = "Maestro";

    private static final String DESCRIPTION_FORMAT = "Woah, 50 contributions to the %s respository...you know what you're doing!!";

    private static final String IMAGE_URL = "https://image.freepik.com/free-vector/golden-trophy_23-2147508492.jpg";

    public MaestroLevelEvaluator() {
        super(COMMIT_THRESHOLD);
    }

    @Override
    String generateDescription(String user, ContributionDetails contributionDetails) {
        return String.format(DESCRIPTION_FORMAT, contributionDetails.getRepoName());
    }

    @Override
    String generateName(String user, ContributionDetails contributionDetails) {
        return NAME;
    }

    @Override
    String getImageUrl() {
        return IMAGE_URL;
    }
}
