import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
    public String encryptString(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] MessageDigest = md.digest(input.getBytes());
        BigInteger bInt = new BigInteger(1, MessageDigest);
        return bInt.toString(16);
    }
}
