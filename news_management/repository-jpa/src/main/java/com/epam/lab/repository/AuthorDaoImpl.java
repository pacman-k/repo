package com.epam.lab.repository;

import com.epam.lab.exception.DAOException;
import com.epam.lab.model.Author;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import java.util.Collections;
import java.util.List;

@Repository
@Validated
public class AuthorDaoImpl implements AuthorDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public Long save(Author author) throws DAOException {
        entityManager.persist(author);
        return author.getId();
    }

    @Override
    public boolean update(Author author) throws DAOException {
        entityManager.merge(author);
        return true;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        entityManager.remove(getById(id));
        return true;
    }

    @Override
    public Author getById(Long id) {
        return entityManager.find(Author.class, id);
    }

    @Override
    public List<Author> getAll() {
        CriteriaQuery<Author> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(Author.class);
        Root<Author> root = criteriaQuery.from(Author.class);
        criteriaQuery.select(root);
        return reverse(entityManager.createQuery(criteriaQuery).getResultList());
    }

    private static <M> List<M> reverse(List<M> list){
         Collections.reverse(list);
         return list;
    }
}
