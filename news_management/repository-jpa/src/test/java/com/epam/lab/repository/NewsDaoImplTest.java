package com.epam.lab.repository;

import com.epam.lab.configuration.DaoConfigTest;
import com.epam.lab.exception.DAOException;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.NewsSearchCriteriaModel;
import com.epam.lab.model.Tag;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoConfigTest.class})
@Transactional
public class NewsDaoImplTest {
    private static final Logger LOGGER = LogManager.getLogger(NewsDaoImplTest.class);

    @Autowired
    private  NewsDao newsDao;
    @PersistenceContext
    private  EntityManager entityManager;


    @Test
    public void save() throws DAOException {
        LOGGER.log(Level.INFO, "--------NEWS SAVE METHOD HAS STARTED-------");
        News news = new News();
        news.setTitle("title");
        news.setShortText("short text");
        news.setFullText("set full text");
        news.setModificationDate(new Date());
        news.setCreationDate(new Date());
        news.getTagIdList().add(1L);
        news.getTagIdList().add(2L);
        news.setAuthorId(5L);
        long id = newsDao.save(news);
        News newsFromDB = entityManager.find(News.class, id);
        assertNotNull(newsFromDB);
        assertEquals(news.getTitle(), newsFromDB.getTitle());
        assertEquals(news.getShortText(), newsFromDB.getShortText());
        assertEquals(news.getFullText(), newsFromDB.getFullText());
        assertEquals(5L, (long) newsFromDB.getAuthorId());
        assertTrue(newsFromDB.getTagIdList().containsAll(news.getTagIdList()));
        LOGGER.log(Level.INFO, "--------NEWS SAVE METHOD HAS FINISHED-------");
    }

    @Test
    public void update() throws DAOException {
        LOGGER.log(Level.INFO, "--------NEWS UPDATE METHOD HAS STARTED-------");
        News newsFromDb = entityManager.find(News.class, 1L);
        System.out.println(newsFromDb.getAuthors());
        entityManager.clear();
        News news = new News();
        news.setId(1L);
        news.setTitle("new title");
        news.setShortText("new short text");
        news.setFullText("new full text");
        news.setModificationDate(new Date());
        news.setCreationDate(newsFromDb.getCreationDate());
        newsDao.update(news);
        News updatedNewsFromDB = entityManager.find(News.class, 1L);
        assertNotEquals(newsFromDb, updatedNewsFromDB);
        assertEquals(news.getTitle(), updatedNewsFromDB.getTitle());
        assertEquals(news.getShortText(), updatedNewsFromDB.getShortText());
        assertEquals(news.getFullText(), updatedNewsFromDB.getFullText());
        LOGGER.log(Level.INFO, "--------NEWS UPDATE METHOD HAS FINISHED-------");
    }

    @Test
    public void delete() throws DAOException {
        LOGGER.log(Level.INFO, "--------NEWS DELETE METHOD HAS STARTED-------");
        News newsFromDB = entityManager.find(News.class, 3L);
        assertNotNull(newsFromDB);
        newsDao.delete(3L);
        assertNull(entityManager.find(News.class, 3L));
        LOGGER.log(Level.INFO, "--------NEWS DELETE METHOD HAS FINISHED-------");
    }

    @Test
    public void getById() {
        LOGGER.log(Level.INFO, "--------NEWS GET_BY_ID METHOD HAS STARTED-------");
        assertEquals(entityManager.find(News.class, 1L), newsDao.getById(1L));
        LOGGER.log(Level.INFO, "--------NEWS GET_BY_ID METHOD HAS FINISHED-------");

    }

    @Test
    public void getAll() {
        LOGGER.log(Level.INFO, "--------NEWS GET_ALL METHOD HAS STARTED-------");
        assertEquals(new HashSet<>(newsDao.getAll()), new HashSet<>(entityManager.createQuery("select news from News news", News.class).getResultList()));
        LOGGER.log(Level.INFO, "--------NEWS GET_ALL METHOD HAS FINISHED-------");
    }

    @Test
    public void getNumberOfNews() {
        LOGGER.log(Level.INFO, "--------NEWS GET_NUMBER_OF_NEWS METHOD HAS STARTED-------");
        assertEquals(newsDao.getNumberOfNews(), (long) entityManager.createQuery("select count(news) from News news", Long.class).getSingleResult());
        LOGGER.log(Level.INFO, "--------NEWS GET_NUMBER_OF_NEWS METHOD HAS FINISHED-------");
    }

    @Test
    public void addTagsToNews() throws DAOException {
        LOGGER.log(Level.INFO, "--------NEWS ADD_TAGS_TO_NEWS METHOD HAS STARTED-------");
        News newsFromDB = entityManager.find(News.class, 8L);
        News news = new News();
        news.setId(8L);
        news.getTagIdList().add(4L);
        news.getTagIdList().add(1L);
        newsDao.addTagsToNews(news);
        entityManager.flush();
        entityManager.clear();
        News updatedNews = entityManager.find(News.class, 8L);
        assertEquals(newsFromDB.getTagIdList().size() + 2, updatedNews.getTagIdList().size());
        LOGGER.log(Level.INFO, "--------NEWS ADD_TAGS_TO_NEWS METHOD HAS FINISHED-------");
    }

    @Test
    public void searchBySpecification() {
        LOGGER.log(Level.INFO, "--------NEWS SEARCH_BY_SPECIFICATION METHOD HAS STARTED-------");
        NewsSearchCriteriaModel criteriaModel1 = new NewsSearchCriteriaModel();
        criteriaModel1.setAuthorDtoName("jOn");
        newsDao.searchBySpecification(new NewsComplexSpecification(criteriaModel1));
        assertEquals(3, newsDao.searchBySpecification(new NewsComplexSpecification(criteriaModel1)).size());
        criteriaModel1.getTags().add("science");
        assertEquals(1, newsDao.searchBySpecification(new NewsComplexSpecification(criteriaModel1)).size());
        NewsSearchCriteriaModel criteriaModel2 = new NewsSearchCriteriaModel();
        criteriaModel2.setAuthorDtoSurname("lanistar");
        assertEquals(2, newsDao.searchBySpecification(new NewsComplexSpecification(criteriaModel2)).size());
        LOGGER.log(Level.INFO, "--------NEWS SEARCH_BY_SPECIFICATION METHOD HAS FINISHED-------");
    }
}