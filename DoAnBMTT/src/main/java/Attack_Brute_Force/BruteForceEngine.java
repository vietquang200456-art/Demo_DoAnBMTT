/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Attack_Brute_Force;
import Demo_WPA2.Crypto;
import Demo_WPA2.WPA2Engine;
import java.util.*;

public class BruteForceEngine {

    public static String crack(String ssid, byte[] anonce, byte[] snonce, byte[] mic, List<String> dict) throws Exception {

        for(String pass : dict){
            byte[] pmk = Crypto.pbkdf2(pass, ssid);
            byte[] ptk = WPA2Engine.genPTK(pmk, anonce, snonce);
            byte[] check = Crypto.hmac(ptk, snonce);

            if(Arrays.equals(check, mic))
                return pass;
        }
        return null;
    }
}