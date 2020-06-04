package com.epam.lab.repository;

import com.epam.lab.model.User;

public interface UsersDao extends DaoCrud<User, Long> {
    User findByLogin(String login);
}
