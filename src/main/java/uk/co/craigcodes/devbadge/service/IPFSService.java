package uk.co.craigcodes.devbadge.service;

/**
 * A service that interacts with an IPFS node to save and retrieve content.
 *
 * @author Craig Williams
 */
public interface IPFSService {

    String saveContent(byte[] content);

    byte[] getContent(String hash);
}
