package uk.co.craigcodes.devbadge.service;

import org.springframework.stereotype.Service;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;
import uk.co.craigcodes.devbadge.DevbadgeException;
import uk.co.craigcodes.devbadge.model.Signature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

@Service
public class DefaultSignatureService implements SignatureService {

    private final static String MNEMONIC_ENV_VARIABLE = "DEVBADGE_MNEMONIC";

    private final static String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    @Override
    public Signature sign(byte[] message) {

        byte[] msgHash = Hash.sha3(prefixMessage(message));

        Sign.SignatureData signedMessage = Sign.signMessage(msgHash, getEcKeyPair(), false);

        return new Signature(new BigInteger(signedMessage.getV()).intValue(),
                Numeric.toHexString(signedMessage.getR()),
                Numeric.toHexString(signedMessage.getS()));
    }

    protected ECKeyPair getEcKeyPair() {
        final Credentials cred = WalletUtils.loadBip39Credentials("", getKeyMnemonic());
        return cred.getEcKeyPair();
    }

    protected String getKeyMnemonic() {
        final String mnemonic = System.getenv(MNEMONIC_ENV_VARIABLE);

        if (mnemonic == null) {
            throw new DevbadgeException(
                    "Unable to read mnemonic.  Ensure the DEVBADGE_MNEMONIC env variable is set");
        }

        return mnemonic;
    }

    private byte[] prefixMessage(byte[] message) {
        // Prefix
        String pref = PERSONAL_MESSAGE_PREFIX + message.length;

        // Concat prefix and message
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

        try {
            outputStream.write(pref.getBytes());
            outputStream.write(message);
        } catch (IOException e) {
            throw new RuntimeException("Error when generating signature", e);
        }

        return outputStream.toByteArray();
    }
}
