package com.epam.lab.repository;


import com.epam.lab.model.News;
import com.epam.lab.model.NewsSearchCriteriaModel;

import java.util.stream.Collectors;


public abstract class AbstractNewsComplexSpecification extends AbstractSpecification<News> {

    NewsSearchCriteriaModel newsSearchCriteriaModel;


    public void setNewsSearchCriteria(NewsSearchCriteriaModel newsSearchCriteriaModel) {
        toLowerRegister(newsSearchCriteriaModel);
        this.newsSearchCriteriaModel = newsSearchCriteriaModel;
    }

    private static void toLowerRegister(NewsSearchCriteriaModel criteriaModel) {
        if (criteriaModel == null) return;
        criteriaModel.setTags(criteriaModel.getTags().stream().map(String::toLowerCase).collect(Collectors.toList()));
        criteriaModel.setAuthorDtoName(criteriaModel.getAuthorDtoName() != null ?
                criteriaModel.getAuthorDtoName().toLowerCase() :
                null);

        criteriaModel.setAuthorDtoSurname(criteriaModel.getAuthorDtoSurname() != null ?
                criteriaModel.getAuthorDtoSurname().toLowerCase() :
                null);
    }
}
