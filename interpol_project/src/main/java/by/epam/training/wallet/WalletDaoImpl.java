package by.epam.training.wallet;

import by.epam.training.core.Bean;
import by.epam.training.dao.ConnectionManager;
import by.epam.training.dao.DAOException;
import by.epam.training.entity.WalletEntity;
import by.epam.training.user.UserDaoException;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Bean
@AllArgsConstructor
public class WalletDaoImpl implements WalletDao {
    private static final String ASSIGN_WALLET = "update user_account set wallet_id=? where id=?";
    private static final String SELECT_ALL_WALLETS = "select wallet_id, wallet_value from wallet";
    private static final String SELECT_WALLET_BY_ID = "select wallet_id, wallet_value from wallet where wallet_id=?";
    private static final String INSERT_WALLET = "insert into wallet (wallet_value) values (?)";
    private static final String UPDATE_WALLET = "update wallet set wallet_value=? where wallet_id=?";
    private static final String DELETE_WALLET = "delete from wallet where wallet_id = ?;";

    ConnectionManager connectionManager;

    @Override
    public void assignWallet(Long walletId, Long userId) throws DAOException {
        try {
            this.assignWallet(userId, walletId, connectionManager.getConnection());
        } catch (SQLException e) {
            throw new WalletDaoException("Cant assign wallet to user : " + userId, e);
        }
    }

    @Override
    public Long save(WalletDto walletDto) throws DAOException {
        WalletEntity entity = fromDto(walletDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(INSERT_WALLET, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setDouble(1, entity.getWalletValue());
            insertStmt.executeUpdate();
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            while (generatedKeys.next()) {
                entity.setWalletId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new WalletDaoException("Cant save wallet :" + entity, e);
        }
        walletDto.setWalletId(entity.getWalletId());
        return entity.getWalletId();
    }

    @Override
    public boolean update(WalletDto walletDto) throws DAOException {
        WalletEntity entity = fromDto(walletDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_WALLET)) {
            int i = 0;
            stmt.setDouble(++i, entity.getWalletValue());
            stmt.setLong(++i, entity.getWalletId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new WalletDaoException("cant update wallet : " + walletDto, e);
        }
    }

    @Override
    public boolean delete(WalletDto walletDto) throws DAOException {
        WalletEntity entity = fromDto(walletDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(DELETE_WALLET)) {
            updateStmt.setLong(1, entity.getWalletId());
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new UserDaoException("Cant wallet user: " + entity, e);
        }
    }

    @Override
    public WalletDto getById(Long id) throws DAOException {
        List<WalletEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_WALLET_BY_ID)) {
            selectStmt.setLong(1, id);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    WalletEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new WalletDaoException("Cant get wallet by id: " + id, e);
        }
        return result.stream()
                .map(this::fromEntity)
                .findFirst().orElseThrow(() -> new WalletDaoException("Cant get role by id: " + id));
    }

    @Override
    public List<WalletDto> getAll() throws DAOException {
        List<WalletEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_ALL_WALLETS)) {
            ResultSet resultSet = selectStmt.executeQuery();
            while (resultSet.next()) {
                result.add(parseResultSet(resultSet));
            }

        } catch (SQLException e) {
            throw new WalletDaoException("Cant find all wallets", e);
        }
        return result.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    private void assignWallet(Long roleId, Long userId, Connection connection) throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement(ASSIGN_WALLET)) {
            int i = 0;
            stmt.setLong(++i, roleId);
            stmt.setLong(++i, userId);
            stmt.executeUpdate();
        }
    }

    private WalletEntity fromDto(WalletDto walletDto) {
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setWalletId(walletDto.getWalletId());
        walletEntity.setWalletValue(walletDto.getWalletValue());
        return walletEntity;
    }

    private WalletDto fromEntity(WalletEntity walletEntity) {
        WalletDto dto = new WalletDto();
        dto.setWalletId(walletEntity.getWalletId());
        dto.setWalletValue(walletEntity.getWalletValue());
        return dto;
    }

    private WalletEntity parseResultSet(ResultSet resultSet) throws SQLException {
        long walletId = resultSet.getLong("wallet_id");
        Double walletValue = resultSet.getDouble("wallet_value");

        return WalletEntity.builder()
                .walletId(walletId)
                .walletValue(walletValue)
                .build();
    }
}
