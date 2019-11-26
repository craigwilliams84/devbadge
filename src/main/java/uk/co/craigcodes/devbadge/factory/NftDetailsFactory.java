package uk.co.craigcodes.devbadge.factory;

import org.springframework.stereotype.Component;
import uk.co.craigcodes.devbadge.model.badge.nft.NftDetails;

@Component
public class NftDetailsFactory implements DevbadgeFactory<NftDetails> {

    @Override
    public NftDetails create() {
        final NftDetails details = new NftDetails();
        details.setContractAddress("0x1234");

        return details;
    }
}
