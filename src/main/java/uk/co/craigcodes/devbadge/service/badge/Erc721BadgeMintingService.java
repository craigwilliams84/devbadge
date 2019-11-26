package uk.co.craigcodes.devbadge.service.badge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;
import uk.co.craigcodes.devbadge.DevbadgeException;
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

            nftDetails.setRedeemed(false);
            //nftDetails.setRedemptionSignature();

            badge.setNftDetails(nftDetails);

            storeMetadata(badge);
        });
    }

    private void storeMetadata(Badge badge) {
        final Erc721Metadata metadata = new Erc721Metadata();
        metadata.setName(badge.getName());
        metadata.setDescription(badge.getDescription());
        metadata.setImageUrl("https://image.freepik.com/free-vector/golden-trophy_23-2147508492.jpg");
        metadata.setExternalUrl("https://www.kauri.io");

        final ObjectMapper mapper = new ObjectMapper();

        try {
            final String hash = ipfsService.saveContent(mapper.writeValueAsBytes(metadata));
            badge.getNftDetails().setMetadataUrl(String.format("https://ipfs.infura.io/5001/ipfs/%s", hash));

        } catch (JsonProcessingException e) {
            throw new DevbadgeException("Unable to save metadata", e);
        }
    }

    private Signature signNftRedemptionProof(Badge badge) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(Numeric.hexStringToByteArray(badge.getOwnerId()));
            outputStream.write(Numeric.hexStringToByteArray(
                    Numeric.toHexStringWithPrefixZeroPadded(badge.getTypeId(), 64)));
            outputStream.write(badge.getNftDetails().getMetadataUrl().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Unable to sign checkpoint", e);
        }

        final byte[] message = Hash.sha3(outputStream.toByteArray());

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
