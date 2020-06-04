package by.epam.training.claimstatus;

import by.epam.training.core.Bean;
import by.epam.training.dao.ConnectionManager;
import by.epam.training.dao.DAOException;
import by.epam.training.entity.ClaimStatusEntity;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Bean
@AllArgsConstructor
public class ClaimStatusDaoImpl implements ClaimStatusDao {
    private static final String SELECT_UND_CONS_STATUS = "select status_id, status_name from claim_status where status_name='under_consideration'";
    private static final String ASSIGN_STATUS = "update claim set status_id=? where id=?";
    private static final String SELECT_ALL_STATUSES = "select status_id, status_name from claim_status";
    private static final String SELECT_STATUS_BY_ID = "select status_id, status_name from claim_status where status_id=?";
    private static final String SELECT_STATUS_BY_NAME = "select status_id, status_name from claim_status where status_name=?";
    private static final String INSERT_NEW_STATUS = "insert into claim_status (status_name) values (?)";

    ConnectionManager connectionManager;

    @Override
    public void assignUndConsStatus(Long claimId) throws DAOException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(SELECT_UND_CONS_STATUS)) {
            final ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                final long statusId = resultSet.getLong(1);
                assignStatus(statusId, claimId, connection);
            }
        } catch (SQLException e) {
            throw new StatusDaoException("Cant assign under_consideration status to claim: " + claimId, e);

        }

    }

    @Override
    public void assignStatus(Long statusId, Long claimId) throws DAOException {
        try {
            this.assignStatus(statusId, claimId, connectionManager.getConnection());
        } catch (SQLException e) {
            throw new StatusDaoException("Cant assign status to claim: " + claimId, e);
        }
    }

    @Override
    public ClaimStatusDto getStatusByName(String statusName) throws DAOException {
        List<ClaimStatusDto> result = new ArrayList<>();
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement stmt = connection.prepareStatement(SELECT_STATUS_BY_NAME)){
            stmt.setString(1, statusName);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                result.add(fromEntity(parseResultSet(resultSet)));
            }
        } catch (SQLException e){
            throw new StatusDaoException("There are no status by such name : " + statusName, e);
        }
        return result.stream().findFirst().orElseThrow(()-> new StatusDaoException("There are no status by such name : " + statusName));
    }

    @Override
    public Long save(ClaimStatusDto claimStatusDto) throws DAOException {
        ClaimStatusEntity statusEntity = fromDto(claimStatusDto);
        try (Connection connection = connectionManager.getConnection()){
            long id = isContainThisStatus(statusEntity.getStatusName(), connection);
            if(id > 0){
                statusEntity.setStatusId(id);
            }else {
                try (PreparedStatement stmt = connection.prepareStatement(INSERT_NEW_STATUS, Statement.RETURN_GENERATED_KEYS)){
                    stmt.setString(1,statusEntity.getStatusName());
                    stmt.executeUpdate();
                    ResultSet resultSet = stmt.getGeneratedKeys();
                    while (resultSet.next()){
                        statusEntity.setStatusId( resultSet.getLong(1));
                    }
                }
            }

        }catch (SQLException e){
            throw new StatusDaoException("Cant save new status : " + statusEntity, e);
        }
        claimStatusDto.setId(statusEntity.getStatusId());
        return statusEntity.getStatusId();    }

    @Override
    public boolean update(ClaimStatusDto claimStatusDto) throws DAOException {
        return false;
    }

    @Override
    public boolean delete(ClaimStatusDto claimStatusDto) throws DAOException {
        return false;
    }

    @Override
    public ClaimStatusDto getById(Long statusId) throws DAOException {
        List<ClaimStatusEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_STATUS_BY_ID)) {
            selectStmt.setLong(1, statusId);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    ClaimStatusEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new StatusDaoException("Cant get status by id: " + statusId, e);
        }
        return  result.stream()
                .map(this::fromEntity)
                .findFirst().orElseThrow(()-> new StatusDaoException("Cant get status by id: " + statusId));
    }

    @Override
    public List<ClaimStatusDto> getAll() throws DAOException {
        List<ClaimStatusEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_ALL_STATUSES)) {
            ResultSet resultSet = selectStmt.executeQuery();
            while (resultSet.next()) {
                result.add(parseResultSet(resultSet));
            }

        } catch (SQLException e) {
            throw new StatusDaoException("Cant find all statuses", e);
        }
        return result.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    private ClaimStatusEntity parseResultSet(ResultSet resultSet) throws SQLException {
        long statusId = resultSet.getLong("status_id");
        String statusName = resultSet.getString("status_name");

        return ClaimStatusEntity.builder()
                .statusId(statusId)
                .statusName(statusName)
                .build();
    }
    private long isContainThisStatus(String statusName, Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_STATUS_BY_NAME)) {
            stmt.setString(1, statusName);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    return resultSet.getLong("status_id");
                }
                return -1;
            }
        }
    }

    private ClaimStatusEntity fromDto(ClaimStatusDto claimStatusDto) {
        ClaimStatusEntity claimStatusEntity = new ClaimStatusEntity();
        claimStatusEntity.setStatusId(claimStatusDto.getId());
        claimStatusEntity.setStatusName(claimStatusDto.getStatus().toString());
        return claimStatusEntity;
    }

    private ClaimStatusDto fromEntity(ClaimStatusEntity entity) {
        ClaimStatusDto dto = new ClaimStatusDto();
        dto.setId(entity.getStatusId());
        dto.setStatus(StatusTypes.getInstance(entity.getStatusName()).orElse(StatusTypes.UNDER_CONSIDERATION));
        return dto;
    }

    private void assignStatus(Long statusId, Long claimId, Connection connection) throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement(ASSIGN_STATUS)) {
            int i = 0;
            stmt.setLong(++i, statusId);
            stmt.setLong(++i, claimId);
            stmt.executeUpdate();
        }
    }
}
