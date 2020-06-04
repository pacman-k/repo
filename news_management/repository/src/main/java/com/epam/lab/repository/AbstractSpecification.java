package com.epam.lab.repository;

import com.epam.lab.exception.NotImplementedException;
import com.epam.lab.model.Model;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;


public abstract class AbstractSpecification<T extends  Model> implements Specification<T> {
    @Override
    public boolean isSatisfiedBy(T t) {
        throw new NotImplementedException();
    }

    @Override
    public Predicate toPredicate( CriteriaBuilder criteriaBuilder, CriteriaQuery<T> criteriaQuery) {
        throw new NotImplementedException();
    }

    @Override
    public String toStringQuery() {
        throw new NotImplementedException();
    }

}
