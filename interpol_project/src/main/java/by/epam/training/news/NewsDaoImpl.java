package by.epam.training.news;

import by.epam.training.core.Bean;
import by.epam.training.dao.ConnectionManager;
import by.epam.training.dao.DAOException;
import by.epam.training.entity.NewsEntity;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Bean
@AllArgsConstructor
public class NewsDaoImpl implements NewsDao {
    private static final String SELECT_ALL_NEWS = "select id, date_of_post, news_text, news_topic, news_heading from news order by  date_of_post desc";
    private static final String SELECT_NEWS_BY_TOPIC = "select id, date_of_post, news_text, news_topic, news_heading from news where news_topic=?";
    private static final String SELECT_NEWS_BY_ID = "select id, date_of_post, news_text, news_topic, news_heading from news where id=?";
    private static final String INSERT_NEWS = "insert into news (date_of_post, news_text, news_topic, news_heading) values (?,?,?,?)";
    private static final String UPDATE_NEWS = "update news set date_of_post=?, news_text=?, news_topic=?, news_heading=? where id=?";
    private static final String DELETE_NEWS = "delete from news where id=?";

    private ConnectionManager connectionManager;

    @Override
    public Long save(NewsDto newsDto) throws DAOException {
        NewsEntity entity = fromDto(newsDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(INSERT_NEWS, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            insertStmt.setTimestamp(++i, entity.getDateOfPost());
            insertStmt.setString(++i, entity.getNewsText());
            insertStmt.setString(++i, entity.getNewsTopic());
            insertStmt.setString(++i, entity.getNewsHeading());
            insertStmt.executeUpdate();
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            while (generatedKeys.next()) {
                entity.setNewsId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new NewsDaoException("Cant save news :" + entity, e);
        }
        newsDto.setNewsId(entity.getNewsId());
        return entity.getNewsId();
    }

    @Override
    public boolean update(NewsDto newsDto) throws DAOException {
        NewsEntity entity = fromDto(newsDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(UPDATE_NEWS)) {
            int i = 0;
            updateStmt.setTimestamp(++i, entity.getDateOfPost());
            updateStmt.setString(++i, entity.getNewsText());
            updateStmt.setString(++i, entity.getNewsTopic());
            updateStmt.setString(++i, entity.getNewsHeading());
            updateStmt.setLong(++i, entity.getNewsId());
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new NewsDaoException("Cant update news: " + entity, e);
        }
    }

    @Override
    public boolean delete(NewsDto newsDto) throws DAOException {
        NewsEntity entity = fromDto(newsDto);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStmt = connection.prepareStatement(DELETE_NEWS)) {
            updateStmt.setLong(1, entity.getNewsId());
            return updateStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new NewsDaoException("Cant delete news: " + entity, e);
        }
    }

    @Override
    public NewsDto getById(Long newsId) throws DAOException {
        List<NewsEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_NEWS_BY_ID)) {
            selectStmt.setLong(1, newsId);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    NewsEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new NewsDaoException("Cant get news by id: " + newsId, e);
        }
        return result.stream()
                .map(this::fromEntity)
                .findFirst().orElseThrow(() -> new NewsDaoException("Cant get news by id: " + newsId));

    }

    @Override
    public List<NewsDto> getAll() throws DAOException {
        List<NewsEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_ALL_NEWS)) {
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    NewsEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new NewsDaoException("Cant find all news", e);
        }
        return result.stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<NewsDto> findNewsByTopic(String newsTopic) throws DAOException {
        List<NewsEntity> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(SELECT_NEWS_BY_TOPIC)) {
            selectStmt.setString(1, newsTopic);
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    NewsEntity entity = parseResultSet(resultSet);
                    result.add(entity);
                }
            }
        } catch (SQLException e) {
            throw new NewsDaoException("Cant get news by topic: " + newsTopic, e);
        }
        return result.stream()
                .map(this::fromEntity).collect(Collectors.toList());

    }
    private NewsEntity parseResultSet(ResultSet resultSet) throws SQLException {
        long entityId = resultSet.getLong("id");
        Timestamp dateOFPost = resultSet.getTimestamp("date_of_post");
        String newsText = resultSet.getString("news_text");
        String newsTopic = resultSet.getString("news_topic");
        String newsHeading = resultSet.getString("news_heading");


        return NewsEntity.builder()
                .newsId(entityId)
                .dateOfPost(dateOFPost)
                .newsText(newsText)
                .newsTopic(newsTopic)
                .newsHeading(newsHeading)
                .build();
    }

    private NewsEntity fromDto(NewsDto dto) {

        NewsEntity entity = new NewsEntity();
        entity.setNewsId(dto.getNewsId());
        entity.setDateOfPost(new Timestamp(dto.getDateOfPost().getTime()));
        entity.setNewsText(dto.getNewsText());
        entity.setNewsTopic(dto.getNewsTopic());
        entity.setNewsHeading(dto.getNewsHeading());
        return entity;
    }

    private NewsDto fromEntity(NewsEntity entity) {

        NewsDto dto = new NewsDto();
        dto.setNewsId(entity.getNewsId());
        dto.setDateOfPost(entity.getDateOfPost());
        dto.setNewsText(entity.getNewsText());
        dto.setNewsTopic(entity.getNewsTopic());
        dto.setNewsHeading(entity.getNewsHeading());
        return dto;
    }

}
