package by.epam.training.security;


public interface CryptographyManager {

    String encode(String text);

    String decode(String encodeText);

}
