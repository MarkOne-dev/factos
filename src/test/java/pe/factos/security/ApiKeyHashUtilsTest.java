package pe.factos.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.factos.security.infrastructure.security.ApiKeyHashUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ApiKeyHashUtilsTest {

    @Test
    @DisplayName("Should generate valid 64-character SHA-256 hex string")
    void shouldGenerateValidSha256Hex() {
        String rawKey = "fct_test_12345";
        String hashedKey = ApiKeyHashUtils.hashApiKey(rawKey);

        assertThat(hashedKey)
                .isNotNull()
                .hasSize(64)
                .matches("^[0-9a-f]{64}$");
    }

    @Test
    @DisplayName("Should return consistent hash for identical inputs")
    void shouldReturnConsistentHashForIdenticalInput() {
        String rawKey = "fct_consistent_key";
        String hash1 = ApiKeyHashUtils.hashApiKey(rawKey);
        String hash2 = ApiKeyHashUtils.hashApiKey(rawKey);

        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    @DisplayName("Should return input directly if already a 64-character hex hash")
    void shouldReturnInputIfAlreadyHexHash() {
        String existingHash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        String result = ApiKeyHashUtils.hashApiKey(existingHash);

        assertThat(result).isEqualTo(existingHash);
    }

    @Test
    @DisplayName("Should return empty string for null or blank input")
    void shouldReturnEmptyForNullOrBlank() {
        assertThat(ApiKeyHashUtils.hashApiKey(null)).isEmpty();
        assertThat(ApiKeyHashUtils.hashApiKey("")).isEmpty();
        assertThat(ApiKeyHashUtils.hashApiKey("   ")).isEmpty();
    }
}
