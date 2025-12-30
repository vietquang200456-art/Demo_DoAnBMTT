package WPA3_Demo;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SAE {

    // prime 256-bit (demo, nhưng đủ mạnh)
    static final BigInteger PRIME = new BigInteger(
            "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16);

    BigInteger scalar;        // private
    BigInteger element;       // commit element
    BigInteger shared;        // shared secret

    BigInteger peerScalar;    // của phía bên kia
    BigInteger peerElement;

    BigInteger PWE;

    public SAE(String ssid, String pass) {
        // PWE = H(SSID||PASS)
        PWE = new BigInteger(1, Crypto.sha((ssid + pass).getBytes())).mod(PRIME);
        if (PWE.equals(BigInteger.ZERO)) PWE = BigInteger.ONE;
    }

    BigInteger rand() {
        BigInteger r;
        do {
            r = new BigInteger(256, new SecureRandom()).mod(PRIME);
        } while (r.equals(BigInteger.ZERO));
        return r;
    }

    // ============ SAE COMMIT ============
    public BigInteger commit() {
        scalar = rand();
        element = scalar.multiply(PWE).mod(PRIME);
        return element;
    }

    // ============ SAE COMPUTE ============
    public BigInteger compute(BigInteger peerElem) {
        peerElement = peerElem;
        shared = peerElem.multiply(scalar).mod(PRIME);
        return shared;
    }

    public void reset() {
        scalar = null;
        element = null;
        shared = null;
        peerScalar = null;
        peerElement = null;
    }
}
