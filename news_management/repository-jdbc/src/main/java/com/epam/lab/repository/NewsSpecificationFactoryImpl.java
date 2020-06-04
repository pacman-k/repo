package com.epam.lab.repository;

import com.epam.lab.model.News;
import com.epam.lab.model.NewsSearchCriteriaModel;
import org.springframework.stereotype.Component;

@Component
public class NewsSpecificationFactoryImpl implements NewsSpecificationFactory {

    @Override
    public Specification<News> getNewsComplexSpecification(NewsSearchCriteriaModel newsSearchCriteriaModel) {
        return new NewsComplexSpecification(newsSearchCriteriaModel);
    }
}
