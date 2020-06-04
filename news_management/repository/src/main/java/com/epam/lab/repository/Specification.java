package com.epam.lab.repository;

import com.epam.lab.model.Model;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;


public interface Specification<T extends Model> {
    boolean isSatisfiedBy(T t);

    Predicate toPredicate(CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery);

    String toStringQuery();
}
