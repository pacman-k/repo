package by.epam.training.wantedPerson;


import by.epam.training.core.Bean;
import by.epam.training.dao.DAOException;
import by.epam.training.dao.TransactionSupport;
import by.epam.training.dao.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Bean
@AllArgsConstructor
@TransactionSupport
public class WantedPersonServiceImpl implements WantedPersonService {
    WantedPersonDao wantedPersonDao;
    @Transactional
    @Override
    public boolean addInfoAboutPerson(WantedPersonDto wantedPersonDto) {

        try {
            wantedPersonDao.save(wantedPersonDto);
            return true;
        }catch (DAOException e){
            log.error("cant add info about wanted person : " + wantedPersonDto, e);
            return false;
        }
    }

    @Override
    public List<WantedPersonDto> getAllPersons() {
        try {
            return wantedPersonDao.getAll();
        }catch (DAOException e){
            log.error("cant get all wanted persons");
            return new ArrayList<>();
        }
    }
}
