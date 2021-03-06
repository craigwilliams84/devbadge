package uk.co.craigcodes.devbadge.model.badge;

import lombok.Data;
import uk.co.craigcodes.devbadge.model.badge.nft.NftDetails;

import java.math.BigInteger;

@Data
public class DefaultAccumulativeCommitBadge implements AccumulativeCommitBadge {

    private String id;

    private String ownerId;

    private BigInteger typeId;

    private String name;

    private String description;

    private String associatedRepository;

    private long timestamp;

    private NftDetails nftDetails;

    private String imageUrl;

    private int commitThreshold;

    @Override
    public boolean isEquivalent(Badge otherBadge) {
        if (!(otherBadge instanceof DefaultAccumulativeCommitBadge)) {
            return false;
        }

        final DefaultAccumulativeCommitBadge accOtherBadge = (DefaultAccumulativeCommitBadge) otherBadge;

        return getOwnerId().equals(accOtherBadge.getOwnerId())
                && getAssociatedRepository().equals(accOtherBadge.getAssociatedRepository())
                && getCommitThreshold() == accOtherBadge.getCommitThreshold();
    }
}
