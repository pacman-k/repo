package com.epam.lab.repository;

import com.epam.lab.exception.DAOException;
import com.epam.lab.model.Tag;

import com.epam.lab.model.Tag_;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Repository
@Validated
public class TagDaoImpl implements TagDao {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(Tag tag) throws DAOException {
        Tag sourceTag = findTagByName(tag.getName());
        if (sourceTag == null) {
            entityManager.persist(tag);
            return tag.getId();
        }
        return sourceTag.getId();
    }

    private Tag findTagByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.lower(root.get(Tag_.NAME)), name.toLowerCase());
        criteriaQuery.select(root).where(predicate);
        return entityManager.createQuery(criteriaQuery).getResultList().stream()
                .findFirst().orElse(null);
    }

    @Override
    public boolean update(Tag tag) throws DAOException {
        entityManager.merge(tag);
        return true;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        entityManager.remove(getById(id));
        return true;
    }

    @Override
    public Tag getById(Long id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public List<Tag> getAll() {
        CriteriaQuery<Tag> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root);
        return reverse(entityManager.createQuery(criteriaQuery).getResultList());
    }

    private static <M> List<M> reverse(List<M> list){
        Collections.reverse(list);
        return list;
    }

}
