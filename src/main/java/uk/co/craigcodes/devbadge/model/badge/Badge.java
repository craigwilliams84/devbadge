package uk.co.craigcodes.devbadge.model.badge;

import uk.co.craigcodes.devbadge.model.badge.nft.NftDetails;

import java.math.BigInteger;

public interface Badge {

    String getId();

    BigInteger getTypeId();

    String getOwnerId();

    String getName();

    String getDescription();

    String getAssociatedRepository();

    long getTimestamp();

    NftDetails getNftDetails();

    void setId(String id);

    void setTypeId(BigInteger typeId);

    void setOwnerId(String ownerId);

    void setName(String name);

    void setDescription(String description);

    void setAssociatedRepository(String associatedRepository);

    void setTimestamp(long timestamp);

    void setNftDetails(NftDetails nftDetails);

    boolean isEquivalent(Badge otherBadge);
}
