package Demo_WPA2;

public class WPA2Engine {

    public static byte[] genPTK(byte[] pmk, byte[] anonce, byte[] snonce) throws Exception {
        byte[] data = concat(anonce, snonce);
        return Crypto.hmac(pmk, data);
    }

    private static byte[] concat(byte[]...a){
        int len=0; for(byte[] x:a) len+=x.length;
        byte[] r=new byte[len]; int p=0;
        for(byte[] x:a){ System.arraycopy(x,0,r,p,x.length); p+=x.length; }
        return r;
    }
}
