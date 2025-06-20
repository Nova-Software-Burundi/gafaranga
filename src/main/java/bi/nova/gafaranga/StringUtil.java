package bi.nova.gafaranga;

import java.security.MessageDigest;
import java.util.UUID;

public class StringUtil {

    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Applies SHA-256 to the input
            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder(); 
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateWalletAddress() {
        String prefix = "wallet_";
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return prefix + randomPart;
    }

}
