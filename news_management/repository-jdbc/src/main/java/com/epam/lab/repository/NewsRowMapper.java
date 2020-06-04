package com.epam.lab.repository;

import com.epam.lab.model.News;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NewsRowMapper implements RowMapper<News> {
    @Override
    public News mapRow(ResultSet resultSet, int i) throws SQLException {
        News news = new News();
        news.setId(resultSet.getLong("id"));
        news.setTitle(resultSet.getString("title").trim());
        news.setShortText(resultSet.getString("short_text").trim());
        news.setFullText(resultSet.getString("full_text").trim());
        news.setCreationDate(resultSet.
                getDate("creation_date"));
        news.setModificationDate(resultSet.
                getDate("modification_date"));
        try {
            long tagId = resultSet.getLong("tag_id");
            news.getTagIdList().add(tagId);
        } catch (Exception e) {
            news.getTagIdList().add(0L);
        }
        return news;
    }
}
