package uk.co.craigcodes.devbadge.service.badge;

import uk.co.craigcodes.devbadge.model.badge.Badge;
import uk.co.craigcodes.devbadge.model.github.ContributionDetails;

import java.util.List;

public interface BadgeMintingService {

    List<Badge> mintBadges(String author, ContributionDetails contributionDetails);
}
