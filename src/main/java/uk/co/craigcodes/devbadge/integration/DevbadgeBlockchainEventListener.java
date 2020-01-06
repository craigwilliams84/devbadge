package uk.co.craigcodes.devbadge.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.consensys.eventeum.dto.block.BlockDetails;
import net.consensys.eventeum.dto.event.ContractEventDetails;
import net.consensys.eventeum.dto.event.parameter.EventParameter;
import net.consensys.eventeum.dto.transaction.TransactionDetails;
import net.consensys.eventeum.integration.broadcast.blockchain.ListenerInvokingBlockchainEventBroadcaster;
import org.springframework.stereotype.Component;
import uk.co.craigcodes.devbadge.service.DevbadgeService;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class DevbadgeBlockchainEventListener
        implements ListenerInvokingBlockchainEventBroadcaster.OnBlockchainEventListener {

    private DevbadgeService devbadgeService;

    @Override
    public void onNewBlock(BlockDetails block) {
        //DO NOTHING
    }

    @Override
    public void onContractEvent(ContractEventDetails eventDetails) {

        try {
            log.info(new ObjectMapper().writeValueAsString(eventDetails));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        final List<EventParameter> nonIndexed = eventDetails.getNonIndexedParameters();
        final List<EventParameter> indexed = eventDetails.getIndexedParameters();

        final BigInteger badgeId = (BigInteger) nonIndexed.get(0).getValue();
        final String owner = indexed.get(0).getValueString();
        final BigInteger typeId = (BigInteger) indexed.get(1).getValue();
        final String url = nonIndexed.get(1).getValueString();

        devbadgeService.handleMintedEvent(badgeId, owner, typeId, url, eventDetails.getTransactionHash());
    }

    @Override
    public void onTransactionEvent(TransactionDetails transactionDetails) {
        //DO NOTHING
    }
}
