package uk.co.craigcodes.devbadge.model.badge.nft;

import lombok.Data;

@Data
public class NftDetails {

    String contractAddress;

    String metadataUrl;

    String redemptionSignature;

    boolean isRedeemed;

    String tokenId;

    String mintTransactionHash;
}
