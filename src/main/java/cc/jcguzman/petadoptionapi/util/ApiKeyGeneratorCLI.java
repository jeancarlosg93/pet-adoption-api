package cc.jcguzman.petadoptionapi.util;

public class ApiKeyGeneratorCLI {
    public static void main(String[] args) {
        System.out.println("\nAPI Key Generator");
        System.out.println("================");

        // Generate different types of keys
        System.out.println("\n1. Simple UUID Key:");
        System.out.println(ApiKeyGenerator.generateUUID());

        System.out.println("\n2. Secure Random Key (32 bytes):");
        System.out.println(ApiKeyGenerator.generateSecureRandomKey(32));

        System.out.println("\n3. Prefixed Keys:");
        System.out.println("Development: " + ApiKeyGenerator.generatePrefixedKey("dev"));
        System.out.println("Production:  " + ApiKeyGenerator.generatePrefixedKey("prod"));
        System.out.println("Testing:     " + ApiKeyGenerator.generatePrefixedKey("test"));

        System.out.println("\n4. Versioned Keys:");
        System.out.println("v1: " + ApiKeyGenerator.generateVersionedKey("v1"));
        System.out.println("v2: " + ApiKeyGenerator.generateVersionedKey("v2"));

        System.out.println("\nNote: Store these keys securely and never commit them to version control.");
    }
}