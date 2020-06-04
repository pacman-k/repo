package com.epam.lab.repository;

import com.epam.lab.exception.DAOException;
import com.epam.lab.exception.RepositoryJDBCException;
import com.epam.lab.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String INSERT_TAG = "insert into tag(name) values (?)";
    private static final String SELECT_ID_BY_NAME = "select id from tag where name=?";
    private static final String UPDATE_TAG = "update tag set name=? where id=?";
    private static final String DELETE_TAG = "delete from tag where id=?";
    private static final String SELECT_TAG_BY_ID = "select tag.*, news_tag.news_id\n"
            + "from tag\n"
            + "left join  news_tag on\n"
            + "tag.id=news_tag.tag_id\n"
            + "where tag.id=?";
    private static final String SELECT_ALL_TAGS_ID = "select id from tag";


    private JdbcOperations template;
    private RowMapper<Tag> tagRowMapper;

    @Autowired
    public TagDaoImpl(JdbcOperations template, RowMapper<Tag> tagRowMapper) {
        this.template = template;
        this.tagRowMapper = tagRowMapper;
    }

    @Override
    public Long save(Tag tag) throws DAOException {
        try {
            List<Long> idList = getListTagIdByName(tag.getName());
            if (isListNotEmpty(idList)) {
                return idList.get(0);
            }
            KeyHolder keyHolder = saveTag(tag);
            return (Long) Objects.requireNonNull(keyHolder.getKeys()).entrySet().stream()
                    .filter(entry -> entry.getKey().equals("id") || entry.getKey().equals("SCOPE_IDENTITY()"))
                    .findFirst()
                    .orElseThrow(() -> new RepositoryJDBCException(DaoMessager.getMessage("dao.commons.id") + tag))
                    .getValue();
        } catch (Exception e) {
            throw new RepositoryJDBCException(DaoMessager.getMessage("dao.tag.save") + tag, e);
        }
    }

    private KeyHolder saveTag(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_TAG, Statement.RETURN_GENERATED_KEYS);
            int i = 0;
            ps.setString(++i, tag.getName());
            return ps;
        }, keyHolder);
        return keyHolder;
    }

    private List<Long> getListTagIdByName(String tagName) {
        return template.query(SELECT_ID_BY_NAME,
                new Object[]{tagName},
                (resultSet, i) -> resultSet.getLong("id"));
    }

    @Override
    public boolean update(Tag tag) throws DAOException {
        try {
            return template.update(UPDATE_TAG, tag.getName(), tag.getId()) > 0;
        } catch (Exception e) {
            throw new RepositoryJDBCException(DaoMessager.getMessage("dao.tag.update") + tag, e);
        }
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        try {
            return template.update(DELETE_TAG, id) > 0;
        } catch (Exception e) {
            throw new RepositoryJDBCException(DaoMessager.getMessage("dao.tag.delete") + id, e);
        }
    }

    @Override
    public Tag getById(Long id) {
        List<Tag> tagList = template.query(SELECT_TAG_BY_ID, new Object[]{id}, tagRowMapper);
        if (tagList.isEmpty()) {
            return null;
        } else {
            return getTagWithAllNews(tagList);
        }
    }

    private Tag getTagWithAllNews(List<Tag> tagList) {
        Tag tag = tagList.remove(0);
        tagList.forEach(streamTag -> tag.getNewsIdList().add(streamTag.getNewsIdList().get(0)));
        return tag;
    }

    @Override
    public List<Tag> getAll() {
        List<Long> idList = template.query(SELECT_ALL_TAGS_ID, (resultSet, i) -> resultSet.getLong("id"));
        return idList.stream().map(this::getById).collect(Collectors.toList());
    }


    private static boolean isListNotEmpty(List list) {
        return !list.isEmpty();
    }
}
