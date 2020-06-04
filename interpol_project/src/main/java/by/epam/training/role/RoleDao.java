package by.epam.training.role;

import by.epam.training.dao.CrudDao;
import by.epam.training.dao.DAOException;


public interface RoleDao extends CrudDao<RoleDto, Long> {
    void assignDefaultRoles(Long userId) throws DAOException;
    void assignRole(Long roleId, Long userId) throws DAOException;
    RoleDto getRoleByName(String roleName) throws DAOException;
}
