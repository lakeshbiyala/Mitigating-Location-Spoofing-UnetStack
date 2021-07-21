import org.arl.fjage.Message
import org.arl.unet.phy.*
import org.arl.unet.localization.*
import org.arl.fjage.*
import org.arl.unet.PDU
import org.arl.unet.*
import java.util.*
import java.math.BigInteger
import java.security.MessageDigest 
import java.security.NoSuchAlgorithmException
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
class temp_ag extends UnetAgent {
    def phy,router,n,txNtf

def timeout=100000
def pdu = PDU.withFormat {
    length(56)
int32('nodeid')
chars('x',22)
chars('y',22)
chars('hash',8)
}
def myloc = PDU.withFormat {
int32('nodeid')
chars('x', 10)
chars('y', 10)
chars('z', 10)
}
public class AES {
 
    private static SecretKeySpec secretKey;
    private static byte[] key;
 
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
  void startup() {
    	phy = agentForService Services.PHYSICAL;
    	
		subscribe topic(phy);
		router = agentForService Services.ROUTING;
  n = agentForService org.arl.unet.Services.NODE_INFO
  
  }

  void processMessage(Message msg) {
    
if (msg instanceof RxFrameNtf && msg.protocol == Protocol.MAC) {
  def prime=[2,3,5,7,9,11,13,17,19]
     final String secKeyx = "5";
        final String secKeyy = "10";
        final String secKeyz = "15";
     
     if(n.address != 5)
    {
     
        String encx = AES.encrypt(Double.toString(n.location.getAt(0)), secKeyx) ;
        String ency = AES.encrypt(Double.toString(n.location.getAt(1)), secKeyy) ;
        int rx= (int)n.location.getAt(0)
        int ry= (int)n.location.getAt(1)
        int rz= (int)n.location.getAt(2)
        double rsum = (rx)+(ry)+(rz)  
    String s1 = Double.toString(rsum)
    String s2 = sha1(s1)
   String rchash = s2
    def bytesa = pdu.encode([nodeid :n.address,x:encx,y:ency,hash:rchash])
      def phy = agentForService Services.PHYSICAL;
      		subscribe topic(phy);
    add new WakerBehavior(1000*prime[n.address], {
    phy << new DatagramReq(to: Address.BROADCAST, protocol: Protocol.MAC, data:bytesa)
     })
    }
    else if(n.address == 5)
    {
    String encx = AES.encrypt(Double.toString(n.location.getAt(0)), secKeyx) ;
    String ency = AES.encrypt(Double.toString(n.location.getAt(1)), secKeyy) ;
        int rx= (int)n.location.getAt(0)
        int ry= (int)n.location.getAt(1)^4
        int rz= (int)n.location.getAt(2)^8
        double rsum = (rx)+(ry)+(rz^8)
    String s1 = Double.toString(rsum)
    String s2 = sha1(s1)
    String rchash = s2
    def bytesa = pdu.encode([nodeid :n.address,x:encx,y:ency,hash:rchash])
      def phy = agentForService Services.PHYSICAL;
      		subscribe topic(phy);
    add new WakerBehavior(100*prime[n.address], {
    phy << new DatagramReq(to: Address.BROADCAST, protocol: Protocol.MAC, data:bytesa)
     })
    }
}

}
public static String sha1(String input)
{
    String sha1 = null
    if(input==null) return null;
    try{
        MessageDigest digest = MessageDigest.getInstance("SHA-1")
        digest.update(input.getBytes(),0,input.length())
        sha1 = new BigInteger(1,digest.digest()).toString(16)
    }
    catch(NoSuchAlgorithmException e){
        e.printStackTrace()
    }
    return sha1
}

}