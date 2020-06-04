package by.epam.training.role;


import by.epam.training.dao.Transactional;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    void assignDefaultRoles(Long userId);

    void assignRole(Long roleId, Long userId);

    Optional<RoleDto> getRoleByName(String roleName);

    @Transactional
    boolean save(RoleDto roleDto);

    Optional<RoleDto> getById(Long id);

    List<RoleDto> getAll();

}
