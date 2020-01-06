package uk.co.craigcodes.devbadge.model.badge.nft;

import lombok.Data;
import uk.co.craigcodes.devbadge.model.Signature;

import java.math.BigInteger;

@Data
public class NftDetails {

    String contractAddress;

    String ownerAddress;

    String metadataUrl;

    Signature redemptionSignature;

    boolean isRedeemed;

    BigInteger tokenId;

    String mintTransactionHash;
}
