package by.epam.training.role;

import by.epam.training.core.Bean;
import by.epam.training.dao.DAOException;
import by.epam.training.dao.TransactionSupport;
import by.epam.training.dao.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j
@Bean
@TransactionSupport
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    RoleDao roleDao;

    @Override
    public void assignDefaultRoles(Long userId) {
        try {
            roleDao.assignDefaultRoles(userId);
        } catch (DAOException e) {
            log.error("Cant assign defaultRole to user : " + userId, e);
        }
    }

    @Override
    public void assignRole(Long roleId, Long userId) {
        try {
            roleDao.assignRole(roleId, userId);
        }catch (DAOException e){
            log.error("Cant assign this role(" + roleId + ") to this user(" + userId + ")",e);
        }

    }

    @Override
    public Optional<RoleDto> getRoleByName(String roleName) {
        try {
            return Optional.of(roleDao.getRoleByName(roleName));
        }catch (DAOException e){
            log.error("Cant get role by this name : " + roleName, e);
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public boolean save(RoleDto roleDto) {
        try {
            roleDao.save(roleDto);
            return true;
        } catch (DAOException e){
            log.error("Cant save role : " + roleDto, e);
            return false;
        }
    }

    @Override
    public Optional<RoleDto> getById(Long id) {
        try {
            return Optional.of(roleDao.getById(id));
        }catch (DAOException e){
            log.error("Cant find role bu such id : " + id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<RoleDto> getAll() {
        try {
            return roleDao.getAll();
        }catch (DAOException e){
            log.error("Cant get all roles", e);
            return new ArrayList<>();
        }
    }
}
