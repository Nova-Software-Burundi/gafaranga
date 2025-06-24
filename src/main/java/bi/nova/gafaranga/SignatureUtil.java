package bi.nova.gafaranga;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignatureUtil {

    public static String signTransaction(String from, String to, BigDecimal amount, String privateKey) {
        String data = from + to + amount + privateKey;
        return sha256(data);
    }

    public static boolean verifyTransaction(String from, String to, BigDecimal amount, String signature, String privateKey) {
        String expected = signTransaction(from, to, amount, privateKey);
        return expected.equals(signature);
    }

    private static String sha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
