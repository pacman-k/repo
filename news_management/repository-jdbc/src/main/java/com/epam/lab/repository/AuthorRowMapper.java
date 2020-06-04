package com.epam.lab.repository;

import com.epam.lab.model.Author;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorRowMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setName(resultSet.getString("name").trim());
        author.setSurname(resultSet.getString("surname").trim());
        long newsId = resultSet.getLong("news_id");
        if (newsId != 0) {
            author.getNewsIdList().add(newsId);
        }
        return author;
    }

}
