package com.epam.lab.repository;

import com.epam.lab.exception.DAOException;
import com.epam.lab.model.User;
import com.epam.lab.model.User_;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Validated
public class UsersDaoImpl implements UsersDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(User user) throws DAOException {
        entityManager.persist(user);
        return user.getId();
    }

    @Override
    public boolean update(User user) throws DAOException {
        entityManager.merge(user);
        return true;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        entityManager.remove(entityManager.find(User.class, id));
        return false;
    }

    @Override
    public User getById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> getAll() {
        CriteriaQuery<User> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public User findByLogin(String login) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.lower(root.get(User_.LOGIN)), login.toLowerCase());
        criteriaQuery.select(root).where(predicate);
        return entityManager.createQuery(criteriaQuery).getResultList().stream()
                .findFirst().orElse(null);
    }
}
