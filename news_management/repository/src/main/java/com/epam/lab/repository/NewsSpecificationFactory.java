package com.epam.lab.repository;


import com.epam.lab.model.News;
import com.epam.lab.model.NewsSearchCriteriaModel;

public interface NewsSpecificationFactory {
    Specification<News> getNewsComplexSpecification(NewsSearchCriteriaModel newsSearchCriteriaModel);
}
