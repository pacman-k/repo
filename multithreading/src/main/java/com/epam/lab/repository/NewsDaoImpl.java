package com.epam.lab.repository;

import com.epam.lab.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.util.List;

@Repository
@Validated
public class NewsDaoImpl implements NewsDao {
    private static final String INSERT_NEWS = "insert into news(title, short_text, full_text, creation_date, modification_date) values (?,?,?,?,?)";

    @PersistenceContext
    private EntityManager entityManager;
    private final JdbcTemplate template;

    @Autowired
    public NewsDaoImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    @Transactional
    public News save(@Valid News news) {
        entityManager.persist(news);
        return news;
    }

    @Override
    @Transactional
    public List<News> saveAll(@Valid List<News> news) {
        news.forEach(this::save);
        return news;
    }

    @Override
    public long getCountOfNews() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<News> root = criteriaQuery.from(News.class);
        criteriaQuery.select(criteriaBuilder.count(root));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
