package uk.co.craigcodes.devbadge.service;

import uk.co.craigcodes.devbadge.exception.NotFoundException;
import uk.co.craigcodes.devbadge.model.badge.Badge;
import uk.co.craigcodes.devbadge.model.user.User;

import java.math.BigInteger;
import java.util.List;

public interface DevbadgeService {

    User loginUser(String accessToken, String reconcileCode);

    User getUserWithCode(String reconcileCode) throws NotFoundException;

    List<Badge> getBadges(String user);

    List<Badge> mintBadgesForRepository(String author, String repoId);

    void handleMintedEvent(
            BigInteger badgeId, String owner, BigInteger typeId, String url, String transactionHash);
}
