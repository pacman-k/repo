package by.epam.training.user;

import by.epam.training.claim.ClaimService;
import by.epam.training.core.Bean;
import by.epam.training.dao.DAOException;
import by.epam.training.dao.TransactionSupport;
import by.epam.training.dao.Transactional;
import by.epam.training.role.RoleDao;
import by.epam.training.role.RoleDto;
import by.epam.training.wallet.WalletDao;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j
@Bean
@AllArgsConstructor
@TransactionSupport
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private RoleDao roleDao;
    private WalletDao walletDao;
    private ClaimService claimService;

    @Override
    public boolean loginUser(UserDto userDto) {
        Optional<UserDto> byLogin;
        try {
            byLogin = userDao.findByLogin(userDto.getLogin());
        } catch (DAOException e) {
            log.error("Failed to read user", e);
            byLogin = Optional.empty();
        }
        return byLogin.filter(dto -> dto.getPassword().equals(userDto.getPassword())).isPresent();
    }

    @Override
    public boolean isLoginTaken(String login) {
        try {
            return userDao.isLoginTaken(login);
        } catch (DAOException e) {
            log.error("Failed in isLoginTaken", e);
            return true;
        }
    }

    @Override
    public boolean isEmailTaken(String email) {
        try {
            return userDao.isEmailTaken(email);
        } catch (DAOException e) {
            log.error("Failed in isPasswordTaken", e);
            return true;
        }
    }

    @Transactional
    @Override
    public boolean registerUser(UserDto userDto) {
        try {
            roleDao.save(userDto.getRoleDto());
            walletDao.save(userDto.getWalletDto());
            userDao.save(userDto);
            return true;
        } catch (DAOException e) {
            log.error("User cant be registered : " + userDto);
            return false;
        }

    }


    @Override
    public List<UserDto> getAllUsers() {
        try {
            return userDao.getAll().stream().map(this::putWalletAndRole).collect(Collectors.toList());
        } catch (DAOException e) {
            log.error("Cant find any users", e);
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<UserDto> findByLogin(String login) {
        try {
            UserDto userDto = userDao.findByLogin(login).orElseThrow(DAOException::new);
            return Optional.of(putWalletAndRole(userDto));

        } catch (DAOException e) {
            log.error("Cant find person with such login : " + login);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        try {
            return Optional.of(putWalletAndRole(userDao.getById(id)));
        } catch (DAOException e) {
            log.error("ERROR there are no user with such id : " + id);
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public boolean deleteUser(UserDto userDto) {
        try {
            claimService.findByCustomer(userDto.getId()).forEach(claimService::deleteClaim);
            userDao.delete(userDto);
            walletDao.delete(userDto.getWalletDto());
            return true;
        } catch (DAOException e) {
            log.error("Cant delete user", e);
            return false;
        }
    }

    @Override
    public boolean lockUser(UserDto userDto) {
        try {
            RoleDto roleDto = roleDao.getRoleByName("locked");
            userDto.setRoleDto(roleDto);
            userDao.update(userDto);
            return true;
        } catch (DAOException e) {
            log.error("cant lock user :" + userDto);
            return false;
        }
    }

    @Override
    public boolean unLockUser(UserDto userDto) {
        try {
            roleDao.assignDefaultRoles(userDto.getId());
            return true;
        } catch (DAOException e) {
            log.error("cant assign default role to " + userDto);
            return false;
        }
    }

    @Override
    public boolean updateUser(UserDto userDto) {
        try {
            userDao.update(userDto);
            return true;
        } catch (DAOException e){
            log.error("cant update user : " + userDto);
            return false;
        }
    }

    private UserDto putWalletAndRole(UserDto userDto) {
        try {
            Long roleId = userDto.getRoleDto().getId();
            userDto.setRoleDto(roleDao.getById(roleId));
            Long walletId = userDto.getWalletDto().getWalletId();
            userDto.setWalletDto(walletDao.getById(walletId));
            return userDto;
        } catch (DAOException e) {
            log.error("cant put wallet and role");
            return userDto;
        }
    }
}
