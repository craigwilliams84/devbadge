package uk.co.craigcodes.devbadge.service.badge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;
import uk.co.craigcodes.devbadge.exception.DevbadgeException;
import uk.co.craigcodes.devbadge.factory.NftDetailsFactory;
import uk.co.craigcodes.devbadge.model.Signature;
import uk.co.craigcodes.devbadge.model.badge.Badge;
import uk.co.craigcodes.devbadge.model.badge.nft.Erc721Metadata;
import uk.co.craigcodes.devbadge.model.badge.nft.NftDetails;
import uk.co.craigcodes.devbadge.model.github.ContributionDetails;
import uk.co.craigcodes.devbadge.repository.BadgeRepository;
import uk.co.craigcodes.devbadge.service.IPFSService;
import uk.co.craigcodes.devbadge.service.SignatureService;
import uk.co.craigcodes.devbadge.service.badge.evaluator.BadgeEvaluator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class Erc721BadgeMintingService implements BadgeMintingService {

    private BadgeRepository badgeRepository;
    private List<BadgeEvaluator> badgeEvaluators;
    private NftDetailsFactory nftDetailsFactory;
    private IPFSService ipfsService;
    private SignatureService signatureService;

    @Override
    public List<Badge> mintBadges(String author, ContributionDetails contributionDetails) {

        final List<Badge> existingBadges = badgeRepository.findByOwnerId(author);

        final List<Badge> awardedBadges = new ArrayList<>();

        badgeEvaluators.forEach(evaluator -> {
            awardedBadges.addAll(
                    evaluator
                        .evaluateContributions(author, contributionDetails)
                        .stream()
                        .filter(badge -> !alreadyIssued(badge, existingBadges))
                        .collect(Collectors.toList())
            );
        });

        initialiseNfts(awardedBadges);

        saveAwardedBadges(awardedBadges);

        return awardedBadges;
    }

    private void initialiseNfts(List<Badge> awardedBadges) {
        awardedBadges.forEach(badge -> {
            final NftDetails nftDetails = nftDetailsFactory.create();

            nftDetails.setOwnerAddress("0x8ef742b9BD0413C3a0eD98E8a85ef84dFcBcBF11");
            nftDetails.setRedeemed(false);

            badge.setNftDetails(nftDetails);

            storeMetadata(badge);
            nftDetails.setRedemptionSignature(signNftRedemptionProof(badge));
        });
    }

    private void storeMetadata(Badge badge) {
        final Erc721Metadata metadata = new Erc721Metadata();
        metadata.setName(badge.getName() + " - " + badge.getAssociatedRepository());
        metadata.setDescription(badge.getDescription());
        metadata.setImageUrl(badge.getImageUrl());
        metadata.setExternalUrl("https://www.kauri.io");

        final ObjectMapper mapper = new ObjectMapper();

        try {
            final String hash = ipfsService.saveContent(mapper.writeValueAsBytes(metadata));
            badge.getNftDetails().setMetadataUrl(String.format("https://ipfs.infura.io/ipfs/%s", hash));

        } catch (JsonProcessingException e) {
            throw new DevbadgeException("Unable to save metadata", e);
        }
    }

    private Signature signNftRedemptionProof(Badge badge) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(Numeric.hexStringToByteArray(badge.getNftDetails().getOwnerAddress()));
            outputStream.write(Numeric.hexStringToByteArray(
                    Numeric.toHexStringWithPrefixZeroPadded(badge.getTypeId(), 64)));
            outputStream.write(badge.getNftDetails().getMetadataUrl().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Unable to sign checkpoint", e);
        }

        final byte[] message = Hash.sha3(outputStream.toByteArray());

        System.out.println("MESSAGE: " + Numeric.toHexString(message));

        return signatureService.sign(message);
    }

    private void saveAwardedBadges(List<Badge> awardedBadges) {
        badgeRepository.saveAll(awardedBadges);
    }

    private boolean alreadyIssued(Badge badge, List<Badge> existingBadges) {
        return existingBadges
                .stream()
                .anyMatch(existing -> existing.isEquivalent(badge));
    }
}
