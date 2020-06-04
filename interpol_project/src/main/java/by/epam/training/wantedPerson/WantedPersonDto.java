package by.epam.training.wantedPerson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.text.SimpleDateFormat;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WantedPersonDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String byName;
    private String country;
    private Date birthday;
    private String description;
    private byte[] photo;

    public boolean hasPhoto(){
        return photo != null && photo.length > 0;
    }

    public String getBase64String() {
       return Base64.getEncoder().encodeToString(photo);
    }
    public String getBirthdayStr(){
        if (birthday == null){
            return null;
        }
        return new SimpleDateFormat("dd-MM-YYYY").format(birthday);
    }
}
