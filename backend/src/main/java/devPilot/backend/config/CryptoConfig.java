// package devPilot.backend.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.crypto.encrypt.Encryptors;
// import org.springframework.security.crypto.encrypt.TextEncryptor;

// @Configuration
// public class CryptoConfig {

//     @Bean
//     TextEncryptor tokenEncryptor(
//             @Value("${app.token-encryptor-password}") String password,
//             @Value("${app.token-encryptor-salt}") String salt) {
//         return Encryptors.text(password, salt);
//     }
// }

package devPilot.backend.config;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesGcmBytesEncryptor;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration
public class CryptoConfig {

    @Bean
    TextEncryptor tokenEncryptor(
            @Value("${app.token-encryptor-password}") String password,
            @Value("${app.token-encryptor-salt}") String salt) {

        AesGcmBytesEncryptor encryptor =
                AesGcmBytesEncryptor.withPassword(password, salt).build();

        return new TextEncryptor() {

            @Override
            public String encrypt(String text) {
                byte[] encrypted = encryptor.encrypt(
                        text.getBytes(StandardCharsets.UTF_8)
                );

                return java.util.HexFormat.of().formatHex(encrypted);
            }

            @Override
            public String decrypt(String encryptedText) {
                byte[] encrypted =
                        java.util.HexFormat.of().parseHex(encryptedText);

                byte[] decrypted = encryptor.decrypt(encrypted);

                return new String(decrypted, StandardCharsets.UTF_8);
            }
        };
    }
}