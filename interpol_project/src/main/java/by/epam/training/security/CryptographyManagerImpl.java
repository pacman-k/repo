package by.epam.training.security;



import by.epam.training.core.Bean;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Bean
public class CryptographyManagerImpl implements CryptographyManager {
    @Override
    public String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String decode(String encodeText) {
        return new String(Base64.getDecoder().decode(encodeText), StandardCharsets.UTF_8);
    }

}
