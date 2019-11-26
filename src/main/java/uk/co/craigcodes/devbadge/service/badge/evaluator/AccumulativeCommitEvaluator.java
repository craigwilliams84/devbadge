package uk.co.craigcodes.devbadge.service.badge.evaluator;

import lombok.AllArgsConstructor;
import org.web3j.crypto.Hash;
import uk.co.craigcodes.devbadge.factory.NftDetailsFactory;
import uk.co.craigcodes.devbadge.model.badge.Badge;
import uk.co.craigcodes.devbadge.model.badge.DefaultAccumulativeCommitBadge;
import uk.co.craigcodes.devbadge.model.github.ContributionDetails;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public abstract class AccumulativeCommitEvaluator implements BadgeEvaluator {

    private int commitThreshold;

    @Override
    public List<Badge> evaluateContributions(String user, ContributionDetails contributionDetails) {
        if (contributionDetails.getCommitCount() >= commitThreshold) {
            final DefaultAccumulativeCommitBadge badge = new DefaultAccumulativeCommitBadge();
            badge.setId(generateId());
            badge.setCommitThreshold(commitThreshold);
            badge.setOwnerId(user);
            badge.setTimestamp(System.currentTimeMillis());
            badge.setName(generateName(user, contributionDetails));
            badge.setDescription(generateDescription(user, contributionDetails));
            badge.setAssociatedRepository(contributionDetails.getRepoName());

            //TODO
            badge.setTypeId(BigInteger.TEN);

            return Collections.singletonList(badge);
        }

        return Collections.emptyList();
    }

    abstract String generateDescription(String user, ContributionDetails contributionDetails);

    abstract String generateName(String user, ContributionDetails contributionDetails);

    private String generateId() {
        return Hash.sha3String(UUID.randomUUID().toString());
    }

}
