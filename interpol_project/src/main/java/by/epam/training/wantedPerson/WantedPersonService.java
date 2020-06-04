package by.epam.training.wantedPerson;

import by.epam.training.dao.Transactional;

import java.util.List;

public interface WantedPersonService {
    @Transactional
    boolean addInfoAboutPerson(WantedPersonDto wantedPersonDto);

    List<WantedPersonDto> getAllPersons();

}
