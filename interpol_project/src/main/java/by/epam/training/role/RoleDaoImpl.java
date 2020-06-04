package by.epam.training.role;

import by.epam.training.core.Bean;
import by.epam.training.dao.ConnectionManager;
import by.epam.training.dao.DAOException;
import by.epam.training.entity.RoleEntity;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Bean
@AllArgsConstructor
public class RoleDaoImpl implements RoleDao {
    private static final String SELECT_DEFAULT_ROLE = "select role_id, role_name from user_role where role_name='default'";
    private static final String ASSIGN_ROLE = "update user_account set role_id=? where id=?";
    private static final String SELECT_ALL_ROLES = "select role_id, role_name from user_role";
    private static final String SELECT_ROLE_BY_ID = "select role_id, role_name from user_role where role_id=?";
    private static final String SELECT_ROLE_BY_NAME = "select role_id, role_name from user_role where role_name=?";
    private static final String INSERT_NEW_ROLE = "insert into user_role (role_name) values (?)";

    ConnectionManager connectionManager;

    @Override
    public void assignDefaultRoles(Long userId) throws DAOException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(SELECT_DEFAULT_ROLE)) {
            final ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                final long roleId = resultSet.getLong(1);
                assignRole(roleId, userId, connection);
            }
        } catch (SQLException e) {
            throw new RoleDaoException("Cant assign default role to user: " + userId, e);

        }
    }

    @Override
    public void assignRole(Long roleId, Long userId) throws DAOException {
        try {
            this.assignRole(roleId, userId, connectionManager.getConnection());
        } catch (SQLException e) {
            throw new RoleDaoException("Cant assign role to user: " + userId, e);
        }

    }

    @Override
    public RoleDto getRoleByName(String roleName) throws DAOException {
        List<RoleDto> result = new ArrayList<>();
        try(Connection connection = connectionManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(SELECT_ROLE_BY_NAME)){
            stmt.setString(1, roleName);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
              result.add(fromEntity(parseResultSet(resultSet)));
            }
        } catch (SQLException e){
            throw new RoleDaoException("There are no role by such name : " + roleName, e);
        }
        return result.stream().findFirst().orElseThrow(()-> new RoleDaoException("There are no role by such name : " + roleName));
    }

    @Override
    public Long save(RoleDto roleDto) throws DAOException {
        RoleEntity roleEntity = fromDto(roleDto);
        try (Connection connection = connectionManager.getConnection()){
            long id = isContainThisRole(roleEntity.getRoleName(), connection);
            if(id > 0){
                roleEntity.setRoleId(id);
            }else {
              try (PreparedStatement stmt = connection.prepareStatement(INSERT_NEW_ROLE, Statement.RETURN_GENERATED_KEYS)){
                  stmt.setString(1,roleEntity.getRoleName());
                  stmt.executeUpdate();
                  ResultSet resultSet = stmt.getGeneratedKeys();
                  while (resultSet.next()){
                   roleEntity.setRoleId( resultSet.getLong(1));
                  }
              }
            }

        }catch (SQLException e){
            throw new RoleDaoException("Cant save new role : " + roleEntity, e);
        }
        roleDto.setId(roleEntity.getRoleId());
        return roleEntity.getRoleId();
    }

    @Override
    public boolean update(RoleDto roleDto) throws DAOException {
        return false;
    }

    @Override
    public boolean delete(RoleDto roleDto) throws DAOException {
        return false;
    }

    @Override
    public RoleDto getById(Long id) throws DAOException {
        List<RoleEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_ROLE_BY_ID)) {
            selectStmt.setLong(1, id);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    RoleEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new RoleDaoException("Cant get role by id: " + id, e);
        }
        return  result.stream()
                .map(this::fromEntity)
                .findFirst().orElseThrow(()-> new RoleDaoException("Cant get role by id: " + id));
    }

    @Override
    public List<RoleDto> getAll() throws DAOException {
        List<RoleEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_ALL_ROLES)) {
            ResultSet resultSet = selectStmt.executeQuery();
                while (resultSet.next()) {
                    result.add(parseResultSet(resultSet));
                }

        } catch (SQLException e) {
            throw new RoleDaoException("Cant find all roles", e);
        }
        return result.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    private long isContainThisRole(String roleName, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_ROLE_BY_NAME)) {
            stmt.setString(1, roleName);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    return resultSet.getLong("role_id");
                }
                return -1;
            }
        }
    }
    private RoleEntity parseResultSet(ResultSet resultSet) throws SQLException {
        long roleId = resultSet.getLong("role_id");
        String roleName = resultSet.getString("role_name");

        return RoleEntity.builder()
                .roleId(roleId)
                .roleName(roleName)
                .build();
    }

    private RoleEntity fromDto(RoleDto roleDto) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleId(roleDto.getId());
        roleEntity.setRoleName(roleDto.getRoleTypes().toString());
        return roleEntity;
    }

    private RoleDto fromEntity(RoleEntity entity) {
        RoleDto dto = new RoleDto();
        dto.setId(entity.getRoleId());
        dto.setRoleTypes(RoleTypes.getInstance(entity.getRoleName()).orElse(RoleTypes.DEFAULT));
        return dto;
    }
    private void assignRole(Long roleId, Long userId, Connection connection) throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement(ASSIGN_ROLE)) {
            int i = 0;
            stmt.setLong(++i, roleId);
            stmt.setLong(++i, userId);
            stmt.executeUpdate();
        }
    }
}
