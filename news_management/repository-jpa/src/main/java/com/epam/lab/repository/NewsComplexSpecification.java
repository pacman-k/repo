package com.epam.lab.repository;


import com.epam.lab.model.*;


import javax.persistence.criteria.*;
import java.util.List;
import java.util.stream.Collectors;

public class NewsComplexSpecification extends AbstractNewsComplexSpecification {


    public NewsComplexSpecification() {
    }

    public NewsComplexSpecification(NewsSearchCriteriaModel newsSearchCriteriaModel) {
        this.setNewsSearchCriteria(newsSearchCriteriaModel);
    }


    @Override
    public Predicate toPredicate(CriteriaBuilder criteriaBuilder, CriteriaQuery<News> criteriaQuery) {
        Root<News> newsRoot = criteriaQuery.from(News.class);
        Predicate tagsPredicate = getTagsPredicate(criteriaQuery, criteriaBuilder, newsRoot);
        Join<News, Author> newsAuthorJoin = newsRoot.joinList(News_.AUTHORS);
        Predicate authorNamePredicate = getAuthorNamePredicate(newsAuthorJoin, criteriaBuilder);
        Predicate authorSurnamePredicate = getAuthorSurnamePredicate(newsAuthorJoin, criteriaBuilder);
        return criteriaBuilder.and(authorNamePredicate, authorSurnamePredicate, tagsPredicate);
    }

    private Predicate getAuthorNamePredicate(Join<News, Author> newsAuthorJoin, CriteriaBuilder criteriaBuilder) {
        Predicate authorNamePredicate = criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        if (isNotEmpty(newsSearchCriteriaModel.getAuthorDtoName())) {
           authorNamePredicate = criteriaBuilder.equal(criteriaBuilder.lower(newsAuthorJoin.get(Author_.NAME)), newsSearchCriteriaModel.getAuthorDtoName());
        }
        return authorNamePredicate;
    }

    private Predicate getAuthorSurnamePredicate(Join<News, Author> newsAuthorJoin, CriteriaBuilder criteriaBuilder) {
        Predicate authorSurnamePredicate = criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        if (isNotEmpty(newsSearchCriteriaModel.getAuthorDtoSurname())) {
            authorSurnamePredicate = criteriaBuilder.
                    equal(criteriaBuilder.lower(newsAuthorJoin.get(Author_.SURNAME)), newsSearchCriteriaModel.getAuthorDtoSurname());
        }
        return authorSurnamePredicate;
    }

    private Predicate getTagsPredicate(CriteriaQuery<News> criteriaQuery, CriteriaBuilder criteriaBuilder, Root<News> newsRoot) {
        Predicate authorTagsPredicate = criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        if (!newsSearchCriteriaModel.getTags().isEmpty()) {
            Subquery<Long> tagSubquery = criteriaQuery.subquery(Long.class);
            Root<Tag> tagRoot = tagSubquery.from(Tag.class);
            ListJoin<Tag, News> subTagNews = tagRoot.joinList(Tag_.NEWS_LIST);
            Predicate predicate = criteriaBuilder.equal(newsRoot.get(News_.ID), subTagNews.get(News_.ID));
            Predicate namePredicate = criteriaBuilder.lower(tagRoot.get(Tag_.NAME)).in(newsSearchCriteriaModel.getTags());
            tagSubquery.select(criteriaBuilder.count(tagRoot.get(Tag_.ID))).where(predicate, namePredicate);
            authorTagsPredicate = criteriaBuilder.greaterThanOrEqualTo(tagSubquery, 1L);
        }

        return authorTagsPredicate;

    }


    private static boolean isNotEmpty(String str) {
        return str != null && !str.trim().equals("");
    }

}
