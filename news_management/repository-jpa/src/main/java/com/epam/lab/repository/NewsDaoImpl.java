package com.epam.lab.repository;

import com.epam.lab.exception.DAOException;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

@Repository
@Validated
public class NewsDaoImpl implements NewsDao {


    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Long save(News news) throws DAOException {
        if (news.getAuthors().isEmpty()) {
            news.getTagIdList().forEach(tadId -> news.getTags().add(entityManager.find(Tag.class, tadId)));
            news.getAuthors().add(entityManager.find(Author.class, news.getAuthorId()));
        }
        entityManager.persist(news);
        return news.getId();
    }

    @Override
    public boolean update(News news) throws DAOException {
        News oldNews = getById(news.getId());
        news.setAuthors(oldNews.getAuthors());
        news.getTags().addAll(oldNews.getTags());
        entityManager.merge(news);
        return true;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        entityManager.remove(getById(id));
        return true;
    }

    @Override
    public News getById(Long id) {
        return entityManager.find(News.class, id);
    }

    @Override
    public List<News> getAll() {
        CriteriaQuery<News> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(News.class);
        Root<News> root = criteriaQuery.from(News.class);
        criteriaQuery.select(root);
        return reverse(entityManager.createQuery(criteriaQuery).getResultList());
    }

    @Override
    public long getNumberOfNews() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<News> root = criteriaQuery.from(News.class);
        criteriaQuery.select(criteriaBuilder.count(root));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public boolean addTagsToNews(News news) throws DAOException {
        News sourceNews = entityManager.find(News.class, news.getId());
        news.getTagIdList()
                .forEach(tadId -> sourceNews.getTags().add(entityManager.find(Tag.class, tadId)));
        return true;
    }

    @Override
    public List<News> searchBySpecification(Specification<News> specification) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Predicate predicate = specification.toPredicate(criteriaBuilder, criteriaQuery);
        criteriaQuery.where(predicate);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private static <M> List<M> reverse(List<M> list) {
        Collections.reverse(list);
        return list;
    }

}
