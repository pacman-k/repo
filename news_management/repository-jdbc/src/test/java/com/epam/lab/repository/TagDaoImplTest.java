package com.epam.lab.repository;

import com.epam.lab.configuration.DaoConfigTest;
import com.epam.lab.exception.DAOException;
import com.epam.lab.model.Tag;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoConfigTest.class})
public class TagDaoImplTest {
    private final static Logger LOGGER = LogManager.getLogger(TagDaoImplTest.class);
    @Autowired
    private  TagDao tagDao;
    @Autowired
    private  JdbcTemplate jt;
    private List<Tag> tags;

    {
        Tag tagNature = new Tag("nature");

        Tag tagSport = new Tag("sport");

        Tag tagCriminal = new Tag("criminal");

        Tag tagPolicy = new Tag("policy");

        tags = Arrays.asList(tagNature, tagSport, tagCriminal, tagPolicy);
    }


    @Test
    public void saveTag() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST SAVE_TAG METHOD HAS STARTED----------");
        int numberOfRowsBefore = jt.query("SELECT id FROM tag;", (resultSet, i) -> resultSet.getLong("id")).size();
        for (Tag tag : tags) {
            tagDao.save(tag);
        }
        int numberOfRowsAfter = jt.query("SELECT id FROM tag;", (resultSet, i) -> resultSet.getLong("id")).size();
        assertEquals(numberOfRowsBefore + tags.size(), numberOfRowsAfter);
        LOGGER.log(Level.INFO, "----------TEST SAVE_TAG METHOD HAS COMPLETED----------");
    }

    @Test
    public void saveDuplicateTag() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST SAVE_DUPLICATE_TAG METHOD HAS STARTED----------");
        int numberOfRowsBefore = jt.query("SELECT id FROM tag;", (resultSet, i) -> resultSet.getInt("id")).size();
        Long firstTagId = tagDao.save(new Tag("flower"));
        Long secondTagId = tagDao.save(new Tag("flower"));
        int numberOfRowsAfter = jt.query("SELECT id FROM tag;", (resultSet, i) -> resultSet.getInt("id")).size();
        assertEquals(firstTagId, secondTagId);
        LOGGER.log(Level.INFO, "----------TEST SAVE_DUPLICATE_TAG METHOD HAS COMPLETED----------");
    }

    @Test
    public void update() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST UPDATE_TAG METHOD HAS STARTED----------");
        String name = "another_name";
        Tag tag = new Tag("cvlification");
        jt.update("INSERT INTO tag (name) values(?);", tag.getName());
        Long id = jt.query("select id from tag;", (rs, i) -> rs.getLong("id")).get(0);
        tag.setName(name);
        tag.setId(id);
        boolean isUpdated = tagDao.update(tag);
        Tag tagFromDb = jt.query("SELECT name FROM tag where id=?;", (resultSet, i) ->
                new Tag(resultSet.getString("name")), id).stream().findFirst().get();
        assertTrue(isUpdated);
        assertTrue(areEquals(tag, tagFromDb));
        LOGGER.log(Level.INFO, "----------TEST UPDATE_TAG METHOD HAS COMPLETED----------");
    }

    @Test
    public void delete() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST DELETE_TAG METHOD HAS STARTED----------");
        int numberOfRowsBefore = jt.query("SELECT id FROM tag;", (resultSet, i) -> resultSet.getInt("id")).size();
        assertTrue(tagDao.delete(1L));
        int numberOfRowsAfter = jt.query("SELECT id FROM tag;", (resultSet, i) -> resultSet.getInt("id")).size();
        assertEquals(numberOfRowsBefore - 1, numberOfRowsAfter);
        LOGGER.log(Level.INFO, "----------TEST DELETE_TAG METHOD HAS COMPLETED----------");
    }

    @Test
    public void deleteNonexistentTag() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST DELETE_NONEXISTENT_TAG METHOD HAS STARTED----------");
        Long randomId = 121L;
        assertFalse(tagDao.delete(randomId));
        LOGGER.log(Level.INFO, "----------TEST DELETE_NONEXISTENT_TAG METHOD HAS COMPLETED----------");
    }

    @Test
    public void getById() {
        LOGGER.log(Level.INFO, "----------TEST GET_BY_ID METHOD HAS STARTED----------");
        List<Long> idList = jt.query("SELECT id FROM tag;", (resultSet, i) -> resultSet.getLong("id"));
        idList.stream().map(tagDao::getById).forEach(Assert::assertNotNull);
        LOGGER.log(Level.INFO, "----------TEST GET_BY_ID METHOD HAS COMPLETED----------");
    }

    @Test
    public void getAll() {
        LOGGER.log(Level.INFO, "----------TEST GET_ALL METHOD HAS STARTED----------");
        List<Tag> tagsFromDb = tagDao.getAll();
        tagsFromDb.stream().forEach(Assert::assertNotNull);
        LOGGER.log(Level.INFO, "----------TEST GET_ALL METHOD HAS COMPLETED----------");
    }


    private boolean areEquals(Tag tag1, Tag tag2) {
        return tag1.getName().equals(tag2.getName());
    }
}