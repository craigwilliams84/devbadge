package uk.co.craigcodes.devbadge.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.co.craigcodes.devbadge.exception.DevbadgeException;
import uk.co.craigcodes.devbadge.exception.NotFoundException;
import uk.co.craigcodes.devbadge.model.badge.Badge;
import uk.co.craigcodes.devbadge.model.badge.nft.NftDetails;
import uk.co.craigcodes.devbadge.model.github.ContributionDetails;
import uk.co.craigcodes.devbadge.model.github.GitHubRepository;
import uk.co.craigcodes.devbadge.model.github.GithubCommit;
import uk.co.craigcodes.devbadge.model.github.GithubUser;
import uk.co.craigcodes.devbadge.model.user.User;
import uk.co.craigcodes.devbadge.repository.BadgeRepository;
import uk.co.craigcodes.devbadge.repository.UserRepository;
import uk.co.craigcodes.devbadge.service.badge.BadgeMintingService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class DefaultDevbadgeService implements DevbadgeService {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    private GithubService githubService;

    private BadgeMintingService badgeMintingService;

    private UserRepository userRepository;

    private BadgeRepository badgeRepository;

    @Override
    public User loginUser(String accessToken, String reconcileCode) {
        try {
            final GithubUser githubUser = githubService.getUser(accessToken);

            final User user =  MODEL_MAPPER.map(githubUser, User.class);
            user.setReconcileCode(reconcileCode);

            return userRepository.save(user);

        } catch (IOException e) {
            throw new DevbadgeException("Unable to retrieve user", e);
        }
    }

    @Override
    public User getUserWithCode(String reconcileCode) throws NotFoundException {
        return userRepository.findByReconcileCode(reconcileCode)
                .orElseThrow(() -> new NotFoundException("User not found with code"));
    }

    @Override
    public List<Badge> getBadges(String user) {
        return badgeRepository.findByOwnerId(user);
    }

    @Override
    public List<Badge> mintBadgesForRepository(String user, String repoName) {
        try {
            final GitHubRepository repo = githubService.getRepository(user, repoName);

            final List<GithubCommit> commits = repo.getCommitsByAuthor(user);

            final ContributionDetails contributionDetails = new ContributionDetails();
            contributionDetails.setRepoName(repoName);
            contributionDetails.setCommitCount(commits.size());

            return badgeMintingService.mintBadges(user, contributionDetails);

        } catch (IOException e) {
            throw new DevbadgeException("Github error", e);
        }
    }

    @Override
    public void handleMintedEvent(BigInteger badgeId,
                                  String owner,
                                  BigInteger typeId,
                                  String url,
                                  String transactionHash) {

        final Optional<Badge> optionalBadge =
                badgeRepository.findByTypeIdAndNftDetailsOwnerAddressAndNftDetailsMetadataUrl(typeId, owner, url);

        if (optionalBadge.isPresent()) {
            final Badge badge = optionalBadge.get();

            final NftDetails nftDetails = badge.getNftDetails();
            nftDetails.setTokenId(badgeId);
            nftDetails.setMintTransactionHash(transactionHash);
            nftDetails.setRedeemed(true);

            badgeRepository.save(badge);
        } else {
            log.warn("Badge not found");
        }
    }
}
