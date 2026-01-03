/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package WPA3_Demo;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Crypto {
    public static byte[] sha(byte[] d){
        try { return MessageDigest.getInstance("SHA-256").digest(d); }
        catch(Exception e){ return new byte[1]; }
    }

    public static byte[] pbkdf2(String pass, String ssid){
        try{
            PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), ssid.getBytes(), 4096, 256);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return f.generateSecret(spec).getEncoded();
        }catch(Exception e){ return new byte[1]; }
    }
    public static byte[] hmac(byte[] key, byte[] data){
        try{
            Mac m = Mac.getInstance("HmacSHA256");
            m.init(new SecretKeySpec(key,"HmacSHA256"));
            return m.doFinal(data);
        }catch(Exception e){ return new byte[1]; }
    }

    public static String hex(byte[] d){
        StringBuilder sb=new StringBuilder();
        for(byte b:d) sb.append(String.format("%02X",b));
        return sb.toString();
    }
}
