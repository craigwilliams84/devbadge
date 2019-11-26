package uk.co.craigcodes.devbadge.service;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.craigcodes.devbadge.DevbadgeException;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * Adapted from net.consensys.mahuta.core.service.storage.ipfs.IPFSService.java.
 *
 * https://github.com/ConsenSys/Mahuta
 */
@Service
@Slf4j
public class DefaultIPFSService implements IPFSService {

    private static final int DEFAULT_THREAD_POOL = 10;
    private static final int DEFAULT_TIMEOUT = 120000;

    private final IPFS ipfs;
    private final ExecutorService pool;
    private final Integer timeout;

    @Autowired
    public DefaultIPFSService(IPFS ipfs) {
        this.ipfs = ipfs;

        this.pool = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL);

        this.timeout = DEFAULT_TIMEOUT;
    }

    @Override
    public String saveContent(byte[] content) {

        log.debug("Store file in IPFS ...");

        try {
            NamedStreamable.ByteArrayWrapper requestFile = new NamedStreamable.ByteArrayWrapper(
                    content);
            MerkleNode response = this.ipfs.add(requestFile).get(0);

            String hash = response.hash.toString();

            log.debug("File created in IPFS: hash={} ", hash);

            return hash;

        } catch (IOException ex) {
            log.error("Exception while storing file in IPFS", ex);
            throw new DevbadgeException(
                    "Exception while storing file in IPFS", ex);
        }
    }

    @Override
    public byte[] getContent(String hash) {

        log.debug("Get file in IPFS [hash: {}] ", hash);

        try {
            Multihash filePointer = Multihash.fromBase58(hash);

            Future<byte[]> ipfsFetcherResult = pool.submit(() -> ipfs.cat(filePointer));

            byte[] content = ipfsFetcherResult.get(timeout, TimeUnit.MILLISECONDS);

            log.debug("Get file in IPFS [hash: {}]", hash);

            return content;

        } catch (java.util.concurrent.TimeoutException ex) {
            log.error("Timeout Exception while getting file in IPFS [hash: {}, timeout: {} ms]", hash, timeout);
            throw new DevbadgeException(
                    "Timeout Exception while getting file in IPFS [hash: " + hash + "]", ex);

        } catch (InterruptedException ex) {
            log.error("Interrupted Exception while getting file in IPFS [hash: {}]", hash);
            throw new DevbadgeException(
                    "Interrupted Exception while getting file in IPFS [hash: " + hash + "]", ex);

        } catch (ExecutionException ex) {
            log.error("Execution Exception while getting file in IPFS [hash: {}]", hash, ex);
            throw new DevbadgeException(
                    "Execution Exception while getting file in IPFS [hash: " + hash + "]", ex);
        }
    }
}
