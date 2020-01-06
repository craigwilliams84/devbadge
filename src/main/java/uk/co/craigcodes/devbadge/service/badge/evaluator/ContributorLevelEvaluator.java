package uk.co.craigcodes.devbadge.service.badge.evaluator;

import org.springframework.stereotype.Component;
import uk.co.craigcodes.devbadge.model.github.ContributionDetails;

@Component
public class ContributorLevelEvaluator extends AccumulativeCommitEvaluator {

    private static final int COMMIT_THRESHOLD = 1;

    private static final String NAME = "Contributor";

    private static final String DESCRIPTION_FORMAT = "You have made at least 1 contribution to the %s repository, keep it going!";

    private static final String IMAGE_URL = "https://p7.hiclipart.com/preview/634/507/744/award-medal-prize-symbol-clip-art-awards.jpg";

    public ContributorLevelEvaluator() {
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
