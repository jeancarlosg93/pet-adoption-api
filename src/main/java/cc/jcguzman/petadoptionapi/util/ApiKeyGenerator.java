package cc.jcguzman.petadoptionapi.util;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public class ApiKeyGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a simple UUID-based API key
     * Format: 550e8400-e29b-41d4-a716-446655440000
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a Base64 encoded random API key
     * Format: dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk
     * @param byteLength Length of random bytes (recommended: 32 or more)
     */
    public static String generateSecureRandomKey(int byteLength) {
        byte[] randomBytes = new byte[byteLength];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * Generates a prefixed API key with timestamp and random component
     * Format: pat_2023_11_04_rB3fKq9zP5hN8vX2
     */
    public static String generatePrefixedKey(String prefix) {
        // Create timestamp component
        String timestamp = Instant.now().toString().split("T")[0].replace("-", "_");

        // Generate random component (12 characters)
        byte[] randomBytes = new byte[9];  // Will result in 12 Base64 characters
        secureRandom.nextBytes(randomBytes);
        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes)
                .substring(0, 12)  // Take first 12 characters
                .replace("-", "K")  // Replace any Base64 special chars
                .replace("_", "Q");

        return String.format("%s_%s_%s", prefix, timestamp, randomPart);
    }

    /**
     * Generates a versioned API key with checksum
     * Format: v1.abcdef0123456789.a1b2c3
     */
    public static String generateVersionedKey(String version) {
        // Generate main key component (16 characters)
        byte[] mainBytes = new byte[12];
        secureRandom.nextBytes(mainBytes);
        String mainPart = Base64.getUrlEncoder().withoutPadding().encodeToString(mainBytes)
                .substring(0, 16)
                .toLowerCase()
                .replace("-", "h")
                .replace("_", "k");

        // Generate checksum (6 characters)
        byte[] checksumBytes = new byte[4];
        secureRandom.nextBytes(checksumBytes);
        String checksumPart = Base64.getUrlEncoder().withoutPadding().encodeToString(checksumBytes)
                .substring(0, 6)
                .toLowerCase()
                .replace("-", "m")
                .replace("_", "n");

        return String.format("%s.%s.%s", version, mainPart, checksumPart);
    }

    public static void main(String[] args) {
        // Example usage
        System.out.println("UUID-based key: " + generateUUID());
        System.out.println("Secure random key: " + generateSecureRandomKey(32));
        System.out.println("Prefixed key: " + generatePrefixedKey("pat"));
        System.out.println("Versioned key: " + generateVersionedKey("v1"));
    }
}