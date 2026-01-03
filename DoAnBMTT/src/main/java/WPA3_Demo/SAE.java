/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WPA3_Demo;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SAE {

    static final BigInteger PRIME = new BigInteger(
            "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);

    BigInteger scalar, element, shared, PWE;

    public SAE(String ssid, String pass) {
        PWE = new BigInteger(1, Crypto.sha((ssid + pass).getBytes())).mod(PRIME);
        if (PWE.equals(BigInteger.ZERO)) {
            PWE = BigInteger.ONE;
        }
    }

    BigInteger rand() {
        return new BigInteger(256, new SecureRandom()).mod(PRIME);
    }

    public BigInteger commit() {
        scalar = rand();
        element = scalar.multiply(PWE).mod(PRIME);
        return element;
    }

//    public BigInteger compute(BigInteger peer) {
//        shared = peer.multiply(scalar).mod(PRIME);
//        return shared;
//    }
    
    public BigInteger compute(BigInteger peerScalar) {
    return shared = element
            .add(peerScalar.multiply(PWE))
            .mod(PRIME)
            .modPow(scalar, PRIME);
}

    public void reset() {
        scalar = null;
        element = null;
        shared = null;
    }

}
