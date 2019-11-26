package uk.co.craigcodes.devbadge.service;

import uk.co.craigcodes.devbadge.model.Signature;

/**
 * A service that provides methods for business logic around signing data.
 */
public interface SignatureService {

    Signature sign(byte[] message);
}
