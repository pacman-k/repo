package com.epam.lab.repository;

import com.epam.lab.exception.*;
import com.epam.lab.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class NewsDaoImpl implements NewsDao {
    private static final String INSERT_NEWS = "insert into news(title, short_text, full_text, creation_date, modification_date) values (?,?,?,?,?)";
    private static final String UPDATE_NEWS = "update news set title=?, short_text=?, full_text=?, modification_date=? where id=?";
    private static final String UPDATE_NEWS_MODIFICATION_DATE = "update news set modification_date=? where id=?";
    private static final String DELETE_NEWS = "delete from news where id=?";
    private static final String SELECT_NEWS_BY_ID = "select news.*, news_tag.tag_id\n"
            + "from news\n"
            + "left join  news_tag on\n"
            + "news.id=news_tag.news_id\n"
            + "where news.id=?";
    private static final String SELECT_ALL_NEWS_ID = "select id from News";
    private static final String INSERT_NEWS_TAG = "insert into news_tag(news_id, tag_id) values (?,?)";
    private static final String INSERT_NEWS_AUTHOR = "insert into news_author(news_id, author_id) values (?,?)";
    private static final String COUNT_OF_NEWS = "select count(id) from news";
    private static final String SELECT_AUTHOR_ID = "select author_id from news_author where news_id=?";


    private JdbcOperations template;
    private RowMapper<News> newsRowMapper;

    @Autowired
    public NewsDaoImpl(JdbcOperations template, RowMapper<News> newsRowMapper) {
        this.template = template;
        this.newsRowMapper = newsRowMapper;
    }

    @Override
    public Long save(News news) throws DAOException {
        try {
            KeyHolder keyHolder = saveNews(news);
            Long newsId = (Long) Objects.requireNonNull(keyHolder.getKeys()).entrySet().stream()
                    .filter(entry -> entry.getKey().equals("id") || entry.getKey().equals("SCOPE_IDENTITY()"))
                    .findFirst()
                    .orElseThrow(() -> new RepositoryJDBCException(DaoMessager.getMessage("dao.common.id") + news))
                    .getValue();
            news.getTagIdList().forEach(id -> linkInNewsTag(newsId, id));
            linkInNewsAuthor(newsId, news.getAuthorId());
            return newsId;
        } catch (Exception e) {
            throw new RepositoryJDBCException(DaoMessager.getMessage("dao.news.save") + news, e);
        }
    }

    private KeyHolder saveNews(News news) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_NEWS, Statement.RETURN_GENERATED_KEYS);
            int i = 0;
            ps.setString(++i, news.getTitle());
            ps.setString(++i, news.getShortText());
            ps.setString(++i, news.getFullText());
            ps.setDate(++i, new Date(news.getCreationDate().getTime()));
            ps.setDate(++i, new Date(news.getModificationDate().getTime()));
            return ps;
        }, keyHolder);
        return keyHolder;
    }

    @Override
    public boolean update(News news) throws DAOException {
        try {
            return template.update(UPDATE_NEWS, news.getTitle(), news.getShortText(), news.getFullText()
                    , news.getModificationDate(), news.getId()) > 0;
        } catch (Exception e) {
            throw new RepositoryJDBCException(DaoMessager.getMessage("dao.news.update") + news, e);
        }
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        try {
            return template.update(DELETE_NEWS, id) > 0;
        } catch (Exception e) {
            throw new RepositoryJDBCException(DaoMessager.getMessage("dao.news.delete") + id, e);
        }
    }

    @Override
    public News getById(Long id) {
        List<News> newsList = template.query(SELECT_NEWS_BY_ID, new Object[]{id}, newsRowMapper);
        if (newsList.isEmpty()) {
            return null;
        } else {
            News news = getNewsWithAllTags(newsList);
            setAuthorId(news);
            return news;
        }
    }

    private News getNewsWithAllTags(List<News> newsList) {
        News news = newsList.remove(0);
        newsList.forEach(streamNews -> news.getTagIdList().add(streamNews.getTagIdList().get(0)));
        return news;
    }

    private void setAuthorId(News news) {
        news.setAuthorId(template.query(SELECT_AUTHOR_ID
                , (resultSet, i) -> resultSet.getLong("author_id")
                , news.getId()).
                stream().findFirst().orElse(0L));
    }

    @Override
    public List<News> getAll() {
        List<Long> idList = template.query(SELECT_ALL_NEWS_ID, (resultSet, i) -> resultSet.getLong("id"));
        return idList.stream().map(this::getById).collect(Collectors.toList());
    }

    @Override
    public long getNumberOfNews() {
        return template.query(COUNT_OF_NEWS, (resultSet, i) -> resultSet.getLong("count"))
                .stream().findFirst().orElse(0L);
    }

    @Override
    public boolean addTagsToNews(News news) throws DAOException {
        if (news.getTagIdList().isEmpty()) return false;
        try {
            news.getTagIdList().forEach(id -> linkInNewsTag(news.getId(), id));
            return template.update(UPDATE_NEWS_MODIFICATION_DATE, news.getModificationDate(), news.getId()) > 0;
        } catch (Exception e) {
            throw new RepositoryJDBCException(DaoMessager.getMessage("dao.news.addTags") + news, e);
        }
    }


    @Override
    public List<News> searchBySpecification(Specification<News> specification) {
        return template.query(specification.toStringQuery(), newsRowMapper);
    }


    private void linkInNewsTag(Long newsId, Long tagId) {
        template.update(INSERT_NEWS_TAG, newsId, tagId);
    }

    private void linkInNewsAuthor(Long newsId, Long authorId) {
        template.update(INSERT_NEWS_AUTHOR, newsId, authorId);
    }

}
