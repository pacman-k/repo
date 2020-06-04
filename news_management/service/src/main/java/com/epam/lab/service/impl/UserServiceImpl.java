package com.epam.lab.service.impl;

import com.epam.lab.dto.Roles;
import com.epam.lab.dto.SecurityUser;
import com.epam.lab.dto.UserDto;
import com.epam.lab.exception.DAOException;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.model.User;
import com.epam.lab.repository.UsersDao;
import com.epam.lab.service.ServiceMessager;
import com.epam.lab.service.UserService;
import com.epam.lab.service.converter.DtoModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class UserServiceImpl implements UserService {

    private UsersDao usersDao;

    private DtoModelConverter<User, UserDto> userConvertor;

    @Autowired
    public UserServiceImpl(UsersDao usersDao, DtoModelConverter<User, UserDto> userConvertor) {
        this.usersDao = usersDao;
        this.userConvertor = userConvertor;
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) throws ServiceException {
        try {
            userDto.getRoles().add(Roles.USER.name());
            Long id = usersDao.save(userConvertor.toModel(userDto));
            return getById(id);
        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.common.save") + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) throws ServiceException {
        try {
            User user = usersDao.getById(userDto.getId());
            if (user == null) {
                throw new ServiceException(ServiceMessager.getMessage("service.common.update")
                        + ServiceMessager.getMessage("service.user.id")
                        + userDto.getId());
            }

            replaceEmptyFields(user, userDto);
            usersDao.update(user);
            return getById(user.getId());
        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.common.update") + e.getMessage(), e);
        }
    }

    private void replaceEmptyFields(User user, UserDto userDto) {
        if (isNotEmpty(userDto.getName())) user.setName(userDto.getName());
        if (isNotEmpty(userDto.getSurname())) user.setSurname(user.getSurname());
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws ServiceException {
        try {
            if (getById(id) == null) {
                throw new ServiceException(ServiceMessager.getMessage("service.common.delete")
                        + ServiceMessager.getMessage("service.user.id")
                        + id);
            }
            return usersDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(ServiceMessager.getMessage("service.common.delete") + e.getMessage(), e);
        }
    }

    @Override
    public UserDto getById(Long id) {
        return userConvertor.toDto(usersDao.getById(id));
    }

    @Override
    public List<UserDto> getAll() {
        return usersDao.getAll().stream().map(userConvertor::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDto user = userConvertor.toDto(usersDao.findByLogin(s));
        if (user == null) throw new UsernameNotFoundException("user '" + s + "' is not exist");
        return new SecurityUser(user);
    }

    private static boolean isNotEmpty(String str) {
        return str != null && !str.trim().equals("");
    }
}
