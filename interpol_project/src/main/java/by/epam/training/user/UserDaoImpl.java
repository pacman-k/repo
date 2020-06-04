package by.epam.training.user;

import by.epam.training.core.Bean;
import by.epam.training.dao.ConnectionManager;
import by.epam.training.dao.DAOException;
import by.epam.training.entity.UserEntity;
import by.epam.training.role.RoleDao;
import by.epam.training.wallet.WalletDao;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Bean
public class UserDaoImpl implements UserDao {

    private static final String SELECT_ALL_QUERY = "select id, login, password, email, first_name, last_name, role_id, wallet_id from user_account";
    private static final String SELECT_BY_ID_QUERY = "select id, login, password, email, first_name, last_name, role_id, wallet_id from user_account where id=?";
    private static final String SELECT_BY_LOGIN_QUERY = "select id, login, password, email, first_name, last_name, role_id, wallet_id from user_account where login=?";
    private static final String SELECT_BY_EMAIL_QUERY = "select id, login, password, email, first_name, last_name, role_id, wallet_id from user_account where email=?";
    private static final String INSERT_QUERY = "insert into user_account (login, password, email, first_name, last_name, role_id, wallet_id) values (?,?,?,?,?,?,?)";
    private static final String UPDATE_QUERY = "update user_account set login=?, password=?, email=?, first_name=?, last_name=?, wallet_id=?, role_id=? where id=?";
    private static final String DELETE_QUERY = "delete from user_account where id = ?";

    private ConnectionManager connectionManager;

    public UserDaoImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public boolean isEmailTaken(String email) throws DAOException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_BY_EMAIL_QUERY)) {
            selectStmt.setString(1, email);
            return selectStmt.executeQuery().next();
        } catch (SQLException e) {
            throw new UserDaoException("Cant find users by email = " + email, e);
        }
    }

    @Override
    public boolean isLoginTaken(String login) throws DAOException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_BY_LOGIN_QUERY)) {
            selectStmt.setString(1, login);
            return selectStmt.executeQuery().next();
        } catch (SQLException e) {

            throw new UserDaoException("Cant find users by login = " + login, e);
        }
    }

    @Override
    public Optional<UserDto> findByLogin(String login) throws DAOException {
        List<UserEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_BY_LOGIN_QUERY)) {
            selectStmt.setString(1, login);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    UserEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new UserDaoException("Cant find users by login = " + login, e);
        }
        return result.stream().map(this::fromEntity).findFirst();
    }


    @Override
    public Long save(UserDto userDto) throws DAOException {
        UserEntity entity = fromDto(userDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            insertStmt.setString(++i, entity.getLogin());
            insertStmt.setString(++i, entity.getPassword());
            insertStmt.setString(++i, entity.getEmail());
            insertStmt.setString(++i, entity.getFirstName());
            insertStmt.setString(++i, entity.getLastName());
            insertStmt.setLong(++i, entity.getRoleId());
            insertStmt.setLong(++i, entity.getWalletId());
            insertStmt.executeUpdate();
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            while (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new UserDaoException("Cant save user :" + entity, e);
        }
        userDto.setId(entity.getId());
        return entity.getId();
    }

    @Override
    public boolean update(UserDto userDto) throws DAOException {
        UserEntity entity = fromDto(userDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            updateStmt.setString(++i, entity.getLogin());
            updateStmt.setString(++i, entity.getPassword());
            updateStmt.setString(++i, entity.getEmail());
            updateStmt.setString(++i, entity.getFirstName());
            updateStmt.setString(++i, entity.getLastName());
            updateStmt.setLong(++i, entity.getWalletId());
            updateStmt.setLong(++i, entity.getRoleId());
            updateStmt.setLong(++i, entity.getId());
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new UserDaoException("Cant update user: " + entity, e);
        }
    }

    @Override
    public boolean delete(UserDto userDto) throws DAOException {
        UserEntity entity = fromDto(userDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(DELETE_QUERY)) {
            updateStmt.setLong(1, entity.getId());
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new UserDaoException("Cant delete user: " + entity, e);
        }
    }

    @Override
    public UserDto getById(Long id) throws DAOException {
        List<UserEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
            selectStmt.setLong(1, id);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    UserEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new UserDaoException("Cant get user by id: " + id, e);
        }
        return result.stream()
                .map(this::fromEntity)
                .findFirst().orElseThrow(() -> new UserDaoException("Cant get user by id: " + id));

    }

    @Override
    public List<UserDto> getAll() throws DAOException {
        List<UserEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_ALL_QUERY)) {
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    UserEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new UserDaoException("Cant find all users", e);
        }
        return result.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    private UserEntity parseResultSet(ResultSet resultSet) throws SQLException {
        long entityId = resultSet.getLong("id");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        String email = resultSet.getString("email");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        long roleId = resultSet.getLong("role_id");
        long walletId = resultSet.getLong("wallet_id");


        return UserEntity.builder()
                .id(entityId)
                .login(login)
                .password(password)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .roleId(roleId)
                .walletId(walletId)
                .build();
    }

    private UserEntity fromDto(UserDto dto) {

        UserEntity entity = new UserEntity();
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setId(dto.getId());
        entity.setLastName(dto.getLastName());
        entity.setLogin(dto.getLogin());
        entity.setPassword(dto.getPassword());
        entity.setRoleId(dto.getRoleDto().getId());
        entity.setWalletId(dto.getWalletDto().getWalletId());

        return entity;
    }

    private UserDto fromEntity(UserEntity entity) {

        UserDto dto = new UserDto();
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setId(entity.getId());
        dto.setLastName(entity.getLastName());
        dto.setLogin(entity.getLogin());
        dto.setPassword(entity.getPassword());
        dto.getRoleDto().setId(entity.getRoleId());
        dto.getWalletDto().setWalletId(entity.getWalletId());


        return dto;
    }
}
