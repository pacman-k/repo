package by.epam.training.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WantedPersonEntity {
    private Long id;
    private String firstName;
    private String lastName;
    private String byName;
    private String country;
    private Timestamp birthday;
    private String description;
    private byte[] photo;
}
