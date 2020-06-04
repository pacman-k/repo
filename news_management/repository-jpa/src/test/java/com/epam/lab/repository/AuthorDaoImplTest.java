package com.epam.lab.repository;

import com.epam.lab.configuration.DaoConfig;
import com.epam.lab.configuration.DaoConfigTest;
import com.epam.lab.exception.DAOException;
import com.epam.lab.model.Author;
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

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoConfigTest.class})
@Transactional
public class AuthorDaoImplTest {

    private static final Logger LOGGER = LogManager.getLogger(AuthorDaoImplTest.class);

    @Autowired
    private AuthorDao authorDao;
    @PersistenceContext
    private EntityManager entityManager;


    @Test
    public void save() throws DAOException {
        LOGGER.log(Level.INFO, "--------AUTHOR SAVE METHOD HAS STARTED-------");
        Author author = new Author();
        author.setName("Jon");
        author.setSurname("Smith");
        Long id = authorDao.save(author);
        assertNotNull(id);
        Author authorFromDB = entityManager.find(Author.class, id);
        assertEquals(author.getName(), authorFromDB.getName());
        assertEquals(author.getSurname(), authorFromDB.getSurname());
        LOGGER.log(Level.INFO, "--------AUTHOR SAVE METHOD HAS FINISHED-------");
    }

    @Test
    public void update() throws DAOException {
        LOGGER.log(Level.INFO, "--------AUTHOR UPDATE METHOD HAS STARTED-------");
        Author authorFromDb = entityManager.find(Author.class, 1L);
        entityManager.clear();
        Author newAuthor = new Author();
        newAuthor.setId(1L);
        newAuthor.setName("newName");
        newAuthor.setSurname("newSurname");
        authorDao.update(newAuthor);
        Author newAuthorFromDB = entityManager.find(Author.class, 1L);
        assertNotEquals(newAuthorFromDB, authorFromDb);
        assertEquals(newAuthor.getName(), newAuthorFromDB.getName());
        assertEquals(newAuthor.getSurname(), newAuthorFromDB.getSurname());
        LOGGER.log(Level.INFO, "--------AUTHOR UPDATE METHOD HAS FINISHED-------");
    }

    @Test
    public void delete() throws DAOException {
        LOGGER.log(Level.INFO, "--------AUTHOR DELETE METHOD HAS STARTED-------");
        Author authorFromDB = entityManager.find(Author.class, 3L);
        assertNotNull(authorFromDB);
        long numberOfNewsBeforeDeleting = entityManager.createQuery("select count(news) from News news", Long.class).getSingleResult();
        long numberOfAuthorNews = authorFromDB.getNewsList().size();
        assertTrue(numberOfAuthorNews > 0);
        authorDao.delete(3L);
        assertNull(entityManager.find(Author.class, 3L));
        long numberOfNewsAfterDeleting = entityManager.createQuery("select count(news) from News news", Long.class).getSingleResult();
        assertEquals(numberOfNewsBeforeDeleting - numberOfAuthorNews, numberOfNewsAfterDeleting);
        LOGGER.log(Level.INFO, "--------AUTHOR DELETE METHOD HAS FINISHED-------");
    }

    @Test
    public void getById() {
        LOGGER.log(Level.INFO, "--------AUTHOR GET_BY_ID METHOD HAS STARTED-------");
        Author author = authorDao.getById(1L);
        Author authorFromDB = entityManager.find(Author.class, 1L);
        assertEquals(authorFromDB, author);
        LOGGER.log(Level.INFO, "--------AUTHOR GET_BY_ID METHOD HAS FINISHED-------");
    }

    @Test
    public void getAll() {
        LOGGER.log(Level.INFO, "--------AUTHOR GET_ALL METHOD HAS STARTED-------");
        List<Author> authors = authorDao.getAll();
        List<Author> authorsFromDB = entityManager.createQuery("select author from Author author", Author.class).getResultList();
        assertEquals(new HashSet<>(authorsFromDB), new HashSet<>(authors));
        LOGGER.log(Level.INFO, "--------AUTHOR GET_ALL METHOD HAS FINISHED-------");
    }


}