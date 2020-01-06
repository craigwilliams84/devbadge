package uk.co.craigcodes.devbadge.configuration;

import net.consensys.eventeum.integration.broadcast.blockchain.BlockchainEventBroadcaster;
import net.consensys.eventeum.integration.broadcast.blockchain.ListenerInvokingBlockchainEventBroadcaster;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.craigcodes.devbadge.integration.DevbadgeBlockchainEventListener;

@Configuration
public class EventeumConfiguration {

    @Bean
    public BlockchainEventBroadcaster listenerBroadcaster(DevbadgeBlockchainEventListener listener) {
        return new ListenerInvokingBlockchainEventBroadcaster(listener);
    }
}
