package com.epam.lab.repository;

import com.epam.lab.configuration.DaoConfigTest;
import com.epam.lab.exception.DAOException;
import com.epam.lab.model.Tag;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.HashSet;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoConfigTest.class})
@Transactional
public class TagDaoImplTest {
    private static final Logger LOGGER = LogManager.getLogger(TagDaoImplTest.class);

    @Autowired
    private TagDao tagDao;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void save() throws DAOException {
        LOGGER.log(Level.INFO, "--------TAG SAVE METHOD HAS STARTED-------");
        Tag tag = new Tag("criminal");
        Tag duplicateTag = new Tag("criminal");
        long tagId = tagDao.save(tag);
        long duplicateTagId = tagDao.save(duplicateTag);
        assertEquals(tagId, duplicateTagId);
        Tag tagFromDB = entityManager.find(Tag.class, tagId);
        assertNotNull(tagFromDB);
        assertEquals(tag.getName(), tagFromDB.getName());
        LOGGER.log(Level.INFO, "--------TAG SAVE METHOD HAS FINISHED-------");
    }

    @Test
    public void update() throws DAOException {
        LOGGER.log(Level.INFO, "--------TAG UPDATE METHOD HAS STARTED-------");
        Tag tagFromDB = entityManager.find(Tag.class, 8L);
        entityManager.clear();
        assertNotNull(tagFromDB);
        Tag tag = new Tag("other name");
        tag.setId(8L);
        tagDao.update(tag);
        Tag updatedTagFromDB = entityManager.find(Tag.class, 8L);
        assertNotEquals(tagFromDB, updatedTagFromDB);
        assertEquals(tag.getName(), updatedTagFromDB.getName());
        LOGGER.log(Level.INFO, "--------TAG UPDATE METHOD HAS FINISHED-------");
    }

    @Test
    public void delete() throws DAOException {
        LOGGER.log(Level.INFO, "--------TAG DELETE METHOD HAS STARTED-------");
        Tag tagFromDB = entityManager.find(Tag.class, 7L);
        assertNotNull(tagFromDB);
        tagDao.delete(7L);
        Tag deletedTagFromDB = entityManager.find(Tag.class, 7L);
        assertNull(deletedTagFromDB);
        LOGGER.log(Level.INFO, "--------TAG DELETE METHOD HAS FINISHED-------");
    }

    @Test
    public void getById() {
        LOGGER.log(Level.INFO, "--------TAG GET_BY_ID METHOD HAS STARTED-------");
        assertEquals(entityManager.find(Tag.class, 1L), tagDao.getById(1L));
        LOGGER.log(Level.INFO, "--------TAG GET_BY_ID METHOD HAS FINISHED-------");
    }

    @Test
    public void getAll() {
        LOGGER.log(Level.INFO, "--------TAG GET_ALL METHOD HAS STARTED-------");
        assertEquals(new HashSet<>(entityManager.createQuery("select tag from Tag tag", Tag.class).getResultList()), new HashSet<>(tagDao.getAll()));
        LOGGER.log(Level.INFO, "--------TAG GET_ALL METHOD HAS FINISHED-------");
    }
}