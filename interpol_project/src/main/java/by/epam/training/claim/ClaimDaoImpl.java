package by.epam.training.claim;

import by.epam.training.claimstatus.ClaimStatusDto;
import by.epam.training.core.Bean;
import by.epam.training.dao.ConnectionManager;
import by.epam.training.dao.DAOException;
import by.epam.training.entity.ClaimEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j
@Bean
@AllArgsConstructor
public class ClaimDaoImpl implements ClaimDao {
    private static final String SELECT_ALL_QUERY = "select id, date_of_claim, cost, status_id, user_account_id, wanted_person_id, founder_id from claim";
    private static final String SELECT_BY_ID_QUERY = "select id, date_of_claim, cost, status_id, user_account_id, wanted_person_id, founder_id from claim where id=?";
    private static final String SELECT_BY_STATUS_QUERY = "select id, date_of_claim, cost, status_id, user_account_id, wanted_person_id, founder_id from claim where status_id=?";
    private static final String SELECT_BY_WANTED_PERSON_QUERY = "select id, date_of_claim, cost, status_id, user_account_id, wanted_person_id, founder_id from claim where wanted_person_id=?";
    private static final String SELECT_BY_CUSTOMER_QUERY = "select id, date_of_claim, cost, status_id, user_account_id, wanted_person_id, founder_id from claim where user_account_id=?";
    private static final String SELECT_BY_LOGIN_QUERY = "select id, login, password, email, first_name, last_name, role_id, wallet_id from user_account where login=?";
    private static final String SELECT_BY_PASSWORD_QUERY = "select id, login, password, email, first_name, last_name, role_id, wallet_id from user_account where password=?";
    private static final String INSERT_QUERY = "insert into claim (date_of_claim, cost, status_id, user_account_id, wanted_person_id) values (?,?,?,?,?)";
    private static final String UPDATE_QUERY = "update claim set date_of_claim=?, cost=?, status_id=? where id=?";
    private static final String DELETE_QUERY = "delete from claim where id = ?";

    private ConnectionManager connectionManager;

    @Override
    public List<ClaimDto> findByCustomer(Long customerId) throws DAOException {
        List<ClaimEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_BY_STATUS_QUERY)) {
            selectStmt.setLong(1, customerId);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    ClaimEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new ClaimDaoException("Cant find claims by customer id = " +customerId, e);
        }
        return result.stream().map(this::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<ClaimDto> getAllByStatus(ClaimStatusDto status) throws DAOException {
        List<ClaimEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_BY_STATUS_QUERY)) {
            selectStmt.setLong(1, status.getId());
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    ClaimEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new ClaimDaoException("Cant find claims by status = " + status.getStatus().toString(), e);
        }
        return result.stream().map(this::fromEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<ClaimDto> findByWantPers(Long persId) throws DAOException {
        List<ClaimEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_BY_WANTED_PERSON_QUERY)) {
            selectStmt.setLong(1, persId);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    ClaimEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new ClaimDaoException("Cant get claim by wated person id: " + persId, e);
        }
        return Optional.of(result.stream()
                .map(this::fromEntity)
                .findFirst().orElseThrow(() -> new ClaimDaoException("Cant get claim by id: " + persId)));
    }

    @Override
    public Long save(ClaimDto claimDto) throws DAOException {
        ClaimEntity entity = fromDto(claimDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            insertStmt.setTimestamp(++i, entity.getDateOfClaim());
            insertStmt.setDouble(++i, entity.getCost());
            insertStmt.setLong(++i, entity.getStatusId());
            insertStmt.setLong(++i, entity.getUserAccountId());
            insertStmt.setLong(++i, entity.getWantedPersonId());
            insertStmt.executeUpdate();
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            while (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            log.error("cant create claim");
            throw new IllegalStateException("something went wrong");
           // throw new ClaimDaoException("Cant save claim :" + entity, e);
        }
        claimDto.setId(entity.getId());
        return entity.getId();
    }

    @Override
    public boolean update(ClaimDto claimDto) throws DAOException {
        ClaimEntity entity = fromDto(claimDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            updateStmt.setTimestamp(++i, entity.getDateOfClaim());
            updateStmt.setDouble(++i, entity.getCost());
            updateStmt.setLong(++i, entity.getStatusId());
            updateStmt.setLong(++i, entity.getId());
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ClaimDaoException("Cant update claim: " + entity, e);
        }
    }

    @Override
    public boolean delete(ClaimDto claimDto) throws DAOException {
        ClaimEntity entity = fromDto(claimDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(DELETE_QUERY)) {
            updateStmt.setLong(1, entity.getId());
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ClaimDaoException("Cant delete claim: " + entity, e);
        }
    }

    @Override
    public ClaimDto getById(Long id) throws DAOException {
        List<ClaimEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
            selectStmt.setLong(1, id);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    ClaimEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new ClaimDaoException("Cant get claim by id: " + id, e);
        }
        return result.stream()
                .map(this::fromEntity)
                .findFirst().orElseThrow(() -> new ClaimDaoException("Cant get claim by id: " + id));
    }

    @Override
    public List<ClaimDto> getAll() throws DAOException {
        List<ClaimEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_ALL_QUERY)) {
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    ClaimEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new ClaimDaoException("Cant find all users", e);
        }
        return result.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }
    private ClaimEntity parseResultSet(ResultSet resultSet) throws SQLException {
        long entityId = resultSet.getLong("id");
        Timestamp dateOfClaim = resultSet.getTimestamp("date_of_claim");
        double cost = resultSet.getDouble("cost");
        long statusId = resultSet.getLong("status_id");
        long userAccountId = resultSet.getLong("user_account_id");
        long wantedPersonId = resultSet.getLong("wanted_person_id");
        long founderId = resultSet.getLong("founder_id");


        return ClaimEntity.builder()
                .id(entityId)
                .dateOfClaim(dateOfClaim)
                .cost(cost)
                .statusId(statusId)
                .userAccountId(userAccountId)
                .wantedPersonId(wantedPersonId)
                .founderId(founderId)
                .build();
    }

    private ClaimEntity fromDto(ClaimDto dto) {

        ClaimEntity entity = new ClaimEntity();
        entity.setDateOfClaim(new Timestamp(dto.getDateOfClaim().getTime()));
        entity.setCost(dto.getCost());
        entity.setId(dto.getId());
        entity.setStatusId(dto.getClaimStatus().getId());
        entity.setUserAccountId(dto.getCustomer().getId());
        entity.setWantedPersonId(dto.getWantedPerson().getId());
        entity.setFounderId(dto.getExecutor() == null ? null : dto.getExecutor().getId());

        return entity;
    }

    private ClaimDto fromEntity(ClaimEntity entity) {

        ClaimDto dto = new ClaimDto();
        dto.setDateOfClaim(entity.getDateOfClaim());
        dto.setCost(entity.getCost());
        dto.setId(entity.getId());
        dto.getClaimStatus().setId(entity.getStatusId());
        dto.getCustomer().setId(entity.getUserAccountId());
        dto.getWantedPerson().setId(entity.getWantedPersonId());
        dto.getExecutor().setId(entity.getFounderId());

        return dto;
    }
}
