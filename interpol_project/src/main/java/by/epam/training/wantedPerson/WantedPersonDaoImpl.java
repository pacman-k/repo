package by.epam.training.wantedPerson;

import by.epam.training.core.Bean;
import by.epam.training.dao.ConnectionManager;
import by.epam.training.dao.DAOException;
import by.epam.training.entity.WantedPersonEntity;
import lombok.AllArgsConstructor;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Bean
@AllArgsConstructor
public class WantedPersonDaoImpl implements WantedPersonDao {
    private static final String SELECT_ALL_PERSON = "select id, first_name, last_name, byname, country, birthday, description, photo from wanted_person";
    private static final String SELECT_BY_ID = "select id, first_name, last_name, byname, country, birthday, description, photo from wanted_person where id=?";
    private static final String INSERT_WANT_PERS = "insert into wanted_person (first_name, last_name, byname, country, birthday, description, photo) values (?,?,?,?,?,?,?)";
    private static final String UPDATE_QUERY = "update wanted_person set first_name=?, last_name=?, byname=?, country=?, birthday=?, description=?, photo=? where id=?";
    private static final String DELETE_QUERY = "delete from wanted_person where id = ?";

    private ConnectionManager connectionManager;

    @Override
    public Long save(WantedPersonDto wantedPersonDto) throws DAOException {
        WantedPersonEntity entity = fromDto(wantedPersonDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(INSERT_WANT_PERS, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            insertStmt.setString(++i, entity.getFirstName());
            insertStmt.setString(++i, entity.getLastName());
            insertStmt.setString(++i, entity.getByName());
            insertStmt.setString(++i, entity.getCountry());
            insertStmt.setTimestamp(++i, entity.getBirthday());
            insertStmt.setString(++i, entity.getDescription());
            insertStmt.setBytes(++i, entity.getPhoto());
            insertStmt.executeUpdate();
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            while (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new WantedPersonDaoException("Cant save wanted person :" + entity, e);
        }
        wantedPersonDto.setId(entity.getId());
        return entity.getId();
    }

    @Override
    public boolean update(WantedPersonDto wantedPersonDto) throws DAOException {
        WantedPersonEntity entity = fromDto(wantedPersonDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(UPDATE_QUERY)) {
            int i = 0;
            updateStmt.setString(++i, entity.getFirstName());
            updateStmt.setString(++i, entity.getLastName());
            updateStmt.setString(++i, entity.getByName());
            updateStmt.setString(++i, entity.getCountry());
            updateStmt.setTimestamp(++i, entity.getBirthday());
            updateStmt.setString(++i, entity.getDescription());
            updateStmt.setBytes(++i, entity.getPhoto());
            updateStmt.setLong(++i, entity.getId());
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new WantedPersonDaoException("Cant update wanted person: " + entity, e);
        }
    }

    @Override
    public boolean delete(WantedPersonDto wantedPersonDto) throws DAOException {
        WantedPersonEntity entity = fromDto(wantedPersonDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(DELETE_QUERY)) {
            updateStmt.setLong(1, entity.getId());
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new WantedPersonDaoException("Cant delete wanted person: " + entity, e);
        }
    }

    @Override
    public WantedPersonDto getById(Long id) throws DAOException {
        List<WantedPersonEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_BY_ID)) {
            selectStmt.setLong(1, id);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    WantedPersonEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException | IOException e) {
            throw new WantedPersonDaoException("Cant get wanted person by id: " + id, e);
        }
        return result.stream()
                .map(this::fromEntity)
                .findFirst().orElseThrow(() -> new WantedPersonDaoException("Cant get wanted person by id: " + id));
    }

    @Override
    public List<WantedPersonDto> getAll() throws DAOException {
        List<WantedPersonEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_ALL_PERSON)) {
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    WantedPersonEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException | IOException e) {
            throw new WantedPersonDaoException("Cant find all wanted persons", e);
        }
        return result.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    private WantedPersonEntity parseResultSet(ResultSet resultSet) throws SQLException, IOException {
        long entityId = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String byname = resultSet.getString("byname");
        String country = resultSet.getString("country");
        Timestamp birthday = resultSet.getTimestamp("birthday");
        String description = resultSet.getString("description");
        byte[] photo = resultSet.getBytes("photo");


        return WantedPersonEntity.builder()
                .id(entityId)
                .firstName(firstName)
                .lastName(lastName)
                .byName(byname)
                .country(country)
                .birthday(birthday)
                .description(description)
                .photo(photo)
                .build();
    }

    private WantedPersonEntity fromDto(WantedPersonDto dto) {

        WantedPersonEntity entity = new WantedPersonEntity();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setByName(dto.getByName());
        entity.setCountry(dto.getCountry());
        entity.setBirthday(dto.getBirthday() == null ? null : new Timestamp(dto.getBirthday().getTime()));
        entity.setDescription(dto.getDescription());
        entity.setPhoto(dto.getPhoto());

        return entity;
    }

    private WantedPersonDto fromEntity(WantedPersonEntity entity) {

        WantedPersonDto dto = new WantedPersonDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setByName(entity.getByName());
        dto.setCountry(entity.getCountry());
        dto.setBirthday(entity.getBirthday());
        dto.setDescription(entity.getDescription());
        dto.setPhoto(entity.getPhoto());

        return dto;
    }
}
