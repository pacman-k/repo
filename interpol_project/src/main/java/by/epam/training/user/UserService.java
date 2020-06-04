package by.epam.training.user;

import by.epam.training.dao.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService {
    boolean loginUser(UserDto userDto);

    boolean isLoginTaken(String login);

    boolean isEmailTaken(String email);

    @Transactional
    boolean registerUser(UserDto userDto);

    List<UserDto> getAllUsers();

    Optional<UserDto> findByLogin(String login);

    Optional<UserDto> findById(Long id);

    @Transactional
    boolean deleteUser(UserDto userDto);

    boolean lockUser(UserDto userDto);

    boolean unLockUser(UserDto userDto);

    boolean updateUser(UserDto userDto);
}
