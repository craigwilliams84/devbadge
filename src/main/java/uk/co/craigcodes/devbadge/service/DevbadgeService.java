package uk.co.craigcodes.devbadge.service;

public interface DevbadgeService {

    void mintBadgesForRepository(String author, String repoId);

    void handleMintedEvent();
}
