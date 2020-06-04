package com.epam.lab.repository;


import com.epam.lab.exception.DAOException;
import com.epam.lab.exception.RepositoryJDBCException;
import com.epam.lab.model.Author;
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
public class AuthorDaoImpl implements AuthorDao {
    private static final String INSERT_AUTHOR = "insert into author(name, surname) values (?,?)";
    private static final String UPDATE_AUTHOR = "update author set name=?, surname=? where id=?";
    private static final String DELETE_AUTHOR = "delete from author where id=?";
    private static final String SELECT_AUTHOR_BY_ID = "select author.*, news_author.news_id\n"
            + "from author\n"
            + "left join  news_author on\n"
            + "author.id=news_author.author_id\n"
            + "where author.id=?";
    private static final String SELECT_ALL_AUTHORS_ID = "select id from author";


    private JdbcOperations template;
    private RowMapper<Author> authorRowMapper;

    @Autowired
    public AuthorDaoImpl(JdbcOperations template, RowMapper<Author> authorRowMapper) {
        this.template = template;
        this.authorRowMapper = authorRowMapper;
    }

    @Override
    public Long save(Author author) throws DAOException {
        try {
            KeyHolder keyHolder = saveAuthor(author);
            return (Long) Objects.requireNonNull(keyHolder.getKeys()).entrySet().stream()
                    .filter(entry -> entry.getKey().equals("id") || entry.getKey().equals("SCOPE_IDENTITY()"))
                    .findFirst()
                    .orElseThrow(() -> new RepositoryJDBCException(DaoMessager.getMessage("dao.common.id") + author))
                    .getValue();
        } catch (Exception e) {
            throw new RepositoryJDBCException(DaoMessager.getMessage("dao.author.save") + author, e);
        }
    }

    private KeyHolder saveAuthor(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS);
            int i = 0;
            ps.setString(++i, author.getName());
            ps.setString(++i, author.getSurname());
            return ps;
        }, keyHolder);
        return keyHolder;
    }

    @Override
    public boolean update(Author author) throws DAOException {
        try {
            return template.update(UPDATE_AUTHOR, author.getName(), author.getSurname(), author.getId()) > 0;
        } catch (Exception e) {
            throw new RepositoryJDBCException(DaoMessager.getMessage("dao.author.update") + author, e);
        }
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        try {
            return template.update(DELETE_AUTHOR, id) > 0;
        } catch (Exception e) {
            throw new RepositoryJDBCException(DaoMessager.getMessage("dao.author.delete") + id, e);
        }
    }

    @Override
    public Author getById(Long id) {
        List<Author> authorList = template.query(SELECT_AUTHOR_BY_ID, new Object[]{id}, authorRowMapper);
        if (authorList.isEmpty()) {
            return null;
        } else {
            return getAuthorWithAllNews(authorList);
        }
    }

    private Author getAuthorWithAllNews(List<Author> authorList) {
        Author author = authorList.remove(0);
        authorList.forEach(streamAuthor -> author.getNewsIdList().add(streamAuthor.getNewsIdList().get(0)));
        return author;
    }

    @Override
    public List<Author> getAll() {
        List<Long> idList = template.query(SELECT_ALL_AUTHORS_ID, (resultSet, i) -> resultSet.getLong("id"));
        return idList.stream().map(this::getById).collect(Collectors.toList());
    }
    
}
