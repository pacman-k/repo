package com.epam.lab.repository;

import com.epam.lab.configuration.DaoConfigTest;
import com.epam.lab.exception.DAOException;
import com.epam.lab.model.Author;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoConfigTest.class})
public class AuthorDaoImplTest {

    private final static Logger LOGGER = LogManager.getLogger(AuthorDaoImplTest.class);
    @Autowired
    private  AuthorDao authorDao;
    @Autowired
    private  JdbcTemplate jt;

    private  List<Author> authors;

     {
        Author authorJon = new Author("Jon", "Smith");
        Author authorAlis = new Author("Alis", "JonFreeman");
        Author authorBody = new Author("Body", "Jenkins");
        Author authorAnge = new Author("Ange", "Bran");
        authors = Arrays.asList(authorJon, authorAlis, authorBody, authorAnge);
    }



    @Test
    public void saveAuthor() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST SAVE_AUTHOR METHOD HAS STARTED----------");
        int numberOfRowsBefore = jt.query("SELECT id FROM author;", (resultSet, i) -> resultSet.getInt("id")).size();
        for (Author author : authors) {
            authorDao.save(author);
        }
        int numberOfRowsAfter = jt.query("SELECT id FROM author;", (resultSet, i) -> resultSet.getInt("id")).size();
        assertEquals(numberOfRowsBefore + authors.size(), numberOfRowsAfter);
        LOGGER.log(Level.INFO, "----------TEST SAVE_AUTHOR METHOD HAS COMPLETED----------");
    }

    @Test
    public void updateAuthor() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST UPDATE_AUTHOR METHOD HAS STARTED----------");
        String name = "another_name";
        String surname = "another_surname";
        Author author = authors.get(0);
        jt.update("INSERT INTO author (name, surname) values(?,?);", author.getName(), author.getSurname());
        Long id = jt.query("SELECT id FROM author", (rs, i) -> rs.getLong("id")).get(0);
        author.setName(name);
        author.setSurname(surname);
        author.setId(id);
        boolean isUpdated = authorDao.update(author);
        Author authorFromDb = jt.query("SELECT name,surname FROM author where id=?;", (resultSet, i) ->
                new Author(resultSet.getString("name"),
                        resultSet.getString("surname")), id).stream().findFirst().get();
        assertTrue(isUpdated);
        assertTrue(areEquals(author, authorFromDb));
        LOGGER.log(Level.INFO, "----------TEST UPDATE_AUTHOR METHOD HAS COMPLETED----------");
    }

    @Test
    public void deleteAuthor() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST DELETE_AUTHOR METHOD HAS STARTED----------");
        Author author = authors.get(0);
        int numberOfRowsBefore = jt.query("SELECT id FROM author;", (resultSet, i) -> resultSet.getInt("id")).size();
        jt.update("INSERT INTO author (name, surname) values(?,?);", author.getName(), author.getSurname());
        Long id = jt.query("select id from author;", (rs, i) -> rs.getLong("id")).get(0);
        assertTrue(authorDao.delete(id));
        int numberOfRowsAfter = jt.query("SELECT id FROM author;", (resultSet, i) -> resultSet.getInt("id")).size();
        assertEquals(numberOfRowsBefore, numberOfRowsAfter);
        LOGGER.log(Level.INFO, "----------TEST DELETE_AUTHOR METHOD HAS COMPLETED----------");
    }

    @Test
    public void deleteNonexistentAuthor() throws DAOException {
        LOGGER.log(Level.INFO, "----------TEST DELETE_NONEXISTENT_AUTHOR METHOD HAS STARTED----------");
        Long randomId = 121L;
        assertFalse(authorDao.delete(randomId));
        LOGGER.log(Level.INFO, "----------TEST DELETE_NONEXISTENT_AUTHOR METHOD HAS COMPLETED----------");
    }

    @Test
    public void getById() {
        LOGGER.log(Level.INFO, "----------TEST GET_BY_ID METHOD HAS STARTED----------");
        Map<Long, Author> authorIdMap = new HashMap<>();
        for (Author author : authors) {
            jt.update("INSERT INTO author (name, surname) values(?,?);", author.getName(), author.getSurname());
            Long id = jt.query("select id from author where id = (select max(id) from author);"
                    , (rs, i) -> rs.getLong("id")).get(0);
            authorIdMap.put(id, author);
        }
        for (Long id : authorIdMap.keySet()) {
            assertTrue(areEquals(authorIdMap.get(id), authorDao.getById(id)));
        }
        LOGGER.log(Level.INFO, "----------TEST GET_BY_ID METHOD HAS COMPLETED----------");
    }

    @Test
    public void getByWrongId() {
        LOGGER.log(Level.INFO, "----------TEST GET_BY_WRONG_ID METHOD HAS STARTED----------");
        Long randomId = 121L;
        assertNull(authorDao.getById(randomId));

        LOGGER.log(Level.INFO, "----------TEST GET_BY_WRONG_ID METHOD HAS COMPLETED----------");
    }

    @Test
    public void getAll() {
        LOGGER.log(Level.INFO, "----------TEST GET_ALL METHOD HAS STARTED----------");
        for (Author author : authors) {
            jt.update("INSERT INTO author (name, surname) values(?,?);", author.getName(), author.getSurname());
        }
        List<Author> authorsFromDb = authorDao.getAll();
        assertTrue(authors.stream()
                .allMatch(author -> authorsFromDb.stream().
                        anyMatch(authorFromDb -> areEquals(author, authorFromDb))));
        LOGGER.log(Level.INFO, "----------TEST GET_ALL METHOD HAS COMPLETED----------");
    }

    private boolean areEquals(Author author1, Author author2) {
        return author1.getName().equals(author2.getName()) &&
                author1.getSurname().equals(author2.getSurname());
    }
}