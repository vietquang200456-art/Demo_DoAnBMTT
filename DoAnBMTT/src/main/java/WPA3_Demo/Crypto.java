package WPA3_Demo;

import java.security.MessageDigest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    public static byte[] sha(byte[] d) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(d);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] hmac(byte[] key, byte[] data) {
        try {
            Mac m = Mac.getInstance("HmacSHA256");
            m.init(new SecretKeySpec(key, "HmacSHA256"));
            return m.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] concat(byte[] a, byte[] b) {
        byte[] r = new byte[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    public static String hex(byte[] d) {
        StringBuilder sb = new StringBuilder();
        for (byte b : d) sb.append(String.format("%02X", b));
        return sb.toString();
    }

    public static byte[] hexToBytes(String s) {
        byte[] r = new byte[s.length() / 2];
        for (int i = 0; i < r.length; i++) {
            r[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        }
        return r;
    }
}
