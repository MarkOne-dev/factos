package pe.factos.security.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class ApiKeyHashUtils {
    private ApiKeyHashUtils() {
    }

    public static String hashApiKey(String rawKey) {
        if (rawKey == null || rawKey.isBlank()) {
            return "";
        }
        // If already a 64-char hex string (e.g. already hashed), return as is
        if (rawKey.length() == 64 && rawKey.matches("^[0-9a-fA-F]{64}$")) {
            return rawKey.toLowerCase();
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawKey.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
