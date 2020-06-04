package com.epam.lab.repository;

import com.epam.lab.exception.DAOException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface DaoCrud <E, K> {
    Long save (@NotNull @Valid E e) throws DAOException;
    boolean update(@NotNull E e)throws DAOException;
    boolean delete (@NotNull K k)throws DAOException;
    E getById (@NotNull K k);
    List<E> getAll();
}
