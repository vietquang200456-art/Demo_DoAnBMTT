package Demo_WPA2;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;

public class Crypto {

    public static byte[] pbkdf2(String pass, String ssid) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), ssid.getBytes(), 4096, 256);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return skf.generateSecret(spec).getEncoded();
    }

    public static byte[] hmac(byte[] key, byte[] data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(key, "HmacSHA1"));
        return mac.doFinal(data);
    }

    public static byte[] random(int n){
        byte[] b = new byte[n];
        new SecureRandom().nextBytes(b);
        return b;
    }

    public static String hex(byte[] b){
        StringBuilder sb=new StringBuilder();
        for(byte x:b) sb.append(String.format("%02X",x));
        return sb.toString();
    }
}
