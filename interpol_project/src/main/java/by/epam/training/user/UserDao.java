package by.epam.training.user;

import by.epam.training.dao.CrudDao;
import by.epam.training.dao.DAOException;

import java.util.Optional;

public interface UserDao extends CrudDao<UserDto, Long> {
    boolean isEmailTaken(String email) throws DAOException;
    boolean isLoginTaken(String login) throws DAOException;
    Optional<UserDto> findByLogin(String login) throws DAOException;
}
