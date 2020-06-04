package by.epam.training.dao;

import java.util.List;

public interface CrudDao <ENT, KEY> {
    KEY save (ENT ent ) throws DAOException;
    boolean update( ENT ent)throws DAOException;
    boolean delete (ENT ent)throws DAOException;
    ENT getById (KEY key)throws DAOException;
    List<ENT> getAll()throws DAOException;
}
