package uk.co.craigcodes.devbadge.service.badge.evaluator;

import uk.co.craigcodes.devbadge.model.badge.Badge;
import uk.co.craigcodes.devbadge.model.github.ContributionDetails;

import java.util.List;

public interface BadgeEvaluator {

    List<Badge> evaluateContributions(String user, ContributionDetails contributionDetails);
}
