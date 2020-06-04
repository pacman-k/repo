package com.epam.lab.repository;

import com.epam.lab.configuration.DaoConfigTest;
import com.epam.lab.exception.DAOException;
import com.epam.lab.model.News;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoConfigTest.class})
public class NewsDaoImplTest {
    private final static Logger LOGGER = LogManager.getLogger(NewsDaoImplTest.class);
    @Autowired
    private  NewsDao newsDao;
    @Autowired
    private  JdbcTemplate jt;
    private  List<News> newsList;

     {
        News newsNature = new News("nature", "nature nature",
                "nature nature nature", new Date(1234123), new Date(341234), 1L);

        News newsSport = new News("sport", "sport sport",
                "sport sport sport", new Date(23532), new Date(12), 2L);

        News newsCriminal = new News("criminal", "criminal criminal",
                "criminal criminal criminal", new Date(534), new Date(35345),
                Arrays.asList(1L, 2L, 3L), 3L);

        News newsPolicy = new News("policy", "policy policy",
                "policy policy policy", new Date(353245), new Date(4535345354L), 4L);

        newsList = Arrays.asList(newsNature, newsSport, newsCriminal, newsCriminal);
    }


    @Test
    public void saveNews() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST SAVE_NEWS METHOD HAS STARTED----------");
        int numberOfRowsBefore = jt.query("SELECT id FROM news;", (resultSet, i) ->
                resultSet.getInt("id")).size();
            newsDao.save(newsList.get(1));

        int numberOfRowsAfter = jt.query("SELECT id FROM news;", (resultSet, i) ->
                resultSet.getInt("id")).size();

        assertNotEquals(numberOfRowsBefore, numberOfRowsAfter);
        LOGGER.log(Level.INFO, "----------TEST SAVE_NEWS METHOD HAS COMPLETED----------");
    }

    @Test
    public void updateNews() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST UPDATE_NEWS METHOD HAS STARTED----------");
        String newTitle = "new title";
        String newShortText = "new short text";
        String newFullText = "new full text";
        News news = newsList.get(0);
        jt.update("INSERT INTO news (title, short_text, full_text, creation_date, modification_date) values(?,?,?,?,?);",
                news.getTitle(), news.getShortText(),
                news.getFullText(), news.getCreationDate(), news.getModificationDate());
        Long id = jt.query("select id from news;", (rs, i) -> rs.getLong("id")).get(0);
        news.setTitle(newTitle);
        news.setShortText(newShortText);
        news.setFullText(newFullText);
        news.setId(id);
        boolean isUpdated = newsDao.update(news);
        News newsFromDb = jt.query("SELECT title,short_text,full_text FROM news where id=?;", (resultSet, i) ->
                new News(resultSet.getString("title"),
                        resultSet.getString("short_text"),
                        resultSet.getString("full_text")), id).stream().findFirst().get();
        assertTrue(isUpdated);
        assertTrue(areEquals(news, newsFromDb));
        LOGGER.log(Level.INFO, "----------TEST UPDATE_NEWS METHOD HAS COMPLETED----------");
    }

    @Test
    public void deleteNews() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST DELETE_NEWS METHOD HAS STARTED----------");
        News news = newsList.get(0);

        jt.update("INSERT INTO news (title, short_text, full_text, creation_date, modification_date) values(?,?,?,?,?);",
                news.getTitle(), news.getShortText(),
                news.getFullText(), news.getCreationDate(), news.getModificationDate());

        Long id = jt.query("select id from news;", (rs, i) -> rs.getLong("id")).get(0);
        int numberOfRowsBefore = jt.query("SELECT id FROM news;", (resultSet, i) -> resultSet.getInt("id")).size();
        assertTrue(newsDao.delete(id));
        int numberOfRowsAfter = jt.query("SELECT id FROM news;", (resultSet, i) -> resultSet.getInt("id")).size();
        assertEquals(numberOfRowsBefore - 1, numberOfRowsAfter);
        LOGGER.log(Level.INFO, "----------TEST DELETE_NEWS METHOD HAS COMPLETED----------");
    }

    @Test
    public void deleteNonexistentNEws() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST DELETE_NONEXISTENT_NEWS METHOD HAS STARTED----------");
        Long randomId = 121L;
        assertFalse(newsDao.delete(randomId));
        LOGGER.log(Level.INFO, "----------TEST DELETE_NONEXISTENT_NEWS METHOD HAS COMPLETED----------");
    }

    @Test
    public void getById() {
        LOGGER.log(Level.INFO, "----------TEST GET_BY_ID METHOD HAS STARTED----------");
        Map<Long, News> newsIdMap = new HashMap<>();
        for (News news : newsList) {
            jt.update("INSERT INTO news (title, short_text, full_text, creation_date, modification_date) values(?,?,?,?,?);",
                    news.getTitle(), news.getShortText(),
                    news.getFullText(), news.getCreationDate(), news.getModificationDate());
            Long id = jt.query("select id from news where id = (select max(id) from news);"
                    , (rs, i) -> rs.getLong("id")).get(0);
            newsIdMap.put(id, news);
        }
        for (Long id : newsIdMap.keySet()) {
            assertTrue(areEquals(newsIdMap.get(id), newsDao.getById(id)));
        }
        LOGGER.log(Level.INFO, "----------TEST GET_BY_ID METHOD HAS COMPLETED----------");
    }

    @Test
    public void getAll() {
        LOGGER.log(Level.INFO, "----------TEST GET_ALL METHOD HAS STARTED----------");
        for (News news : newsList) {
            jt.update("INSERT INTO news (title, short_text, full_text, creation_date, modification_date) values(?,?,?,?,?);",
                    news.getTitle(), news.getShortText(),
                    news.getFullText(), news.getCreationDate(), news.getModificationDate());
        }
        List<News> newsFromDb = newsDao.getAll();
        assertTrue(newsList.stream()
                .allMatch(news -> newsFromDb.stream().
                        anyMatch(unoNewsFromDb -> areEquals(news, unoNewsFromDb))));
        LOGGER.log(Level.INFO, "----------TEST GET_ALL METHOD HAS COMPLETED----------");
    }

    @Test
    public void addTagsToNews() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST ADD_TAGS_TO_NEWS METHOD HAS STARTED----------");
        News news = newsList.stream().filter(news1 ->
                !news1.getTagIdList().isEmpty()).findFirst().get();
        int numberOfRowsBefore = jt.query("select tag_id from news_tag", (set, i) -> set.getLong("tag_id")).size();
        jt.update("INSERT INTO news (title, short_text, full_text, creation_date, modification_date) values(?,?,?,?,?);",
                news.getTitle(), news.getShortText(),
                news.getFullText(), news.getCreationDate(), news.getModificationDate());
        Long id = jt.query("select id from news where id = (select max(id) from news);"
                , (rs, i) -> rs.getLong("id")).get(0);
        news.setId(id);
        assertTrue(newsDao.addTagsToNews(news));
        int numberOfRowsAfter = jt.query("select tag_id from news_tag", (set, i) -> set.getLong("tag_id")).size();
        assertEquals(numberOfRowsBefore + news.getTagIdList().size(), numberOfRowsAfter);
        LOGGER.log(Level.INFO, "----------TEST ADD_TAGS_TO_NEWS METHOD HAS COMPLETED----------");
    }

    private boolean areEquals(News news1, News news2) {
        return news1.getTitle().equals(news2.getTitle()) &&
                news1.getShortText().equals(news2.getShortText()) &&
                news1.getFullText().equals(news2.getFullText());
    }
}