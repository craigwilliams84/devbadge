package uk.co.craigcodes.devbadge.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import uk.co.craigcodes.devbadge.DevbadgeException;
import uk.co.craigcodes.devbadge.model.github.ContributionDetails;
import uk.co.craigcodes.devbadge.model.github.GitHubRepository;
import uk.co.craigcodes.devbadge.model.github.GithubCommit;
import uk.co.craigcodes.devbadge.service.badge.BadgeMintingService;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class DefaultDevbadgeService implements DevbadgeService {

    private GithubService githubService;

    private BadgeMintingService badgeMintingService;

    @Override
    public void mintBadgesForRepository(String author, String repoName) {
        try {
            final GitHubRepository repo = githubService.getRepository(repoName);

            final List<GithubCommit> commits = repo.getCommitsByAuthor(author);

            final ContributionDetails contributionDetails = new ContributionDetails();
            contributionDetails.setRepoName(repoName);
            contributionDetails.setCommitCount(commits.size());

            badgeMintingService.mintBadges(author, contributionDetails);

        } catch (IOException e) {
            throw new DevbadgeException("Github error", e);
        }
    }

    @Override
    public void handleMintedEvent() {

    }
}
