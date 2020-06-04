package com.epam.lab.repository;


import com.epam.lab.model.NewsSearchCriteriaModel;

import java.util.List;


public class NewsComplexSpecification extends AbstractNewsComplexSpecification {

    private static final String SELECTING_QUERY = "SELECT news.id, news.title," +
            " news.short_text, news.full_text, news.creation_date, news.modification_date, tag.id" +
            " FROM news JOIN news_author " +
            "ON news.id = news_author.news_id JOIN author " +
            "ON author.id = news_author.author_id JOIN news_tag " +
            "ON news.id = news_tag.news_id JOIN tag " +
            "ON news_tag.tag_id = tag.id WHERE true=true ";

    private static final String MISSING = "true=true ";

    public NewsComplexSpecification() {
    }

    public NewsComplexSpecification(NewsSearchCriteriaModel newsSearchCriteriaModel) {
        this.setNewsSearchCriteria(newsSearchCriteriaModel);
    }

    @Override
    public String toStringQuery() {
        StringBuilder stringBuilder = new StringBuilder(SELECTING_QUERY);
        stringBuilder.append(getAuthorNameQuery());
        stringBuilder.append(getAuthorSurnameQuery());
        stringBuilder.append(getTagsQuery());
        return stringBuilder.append(";").toString();
    }

    private StringBuilder getAuthorNameQuery() {
        StringBuilder stringBuilder = new StringBuilder("and ");
        return isNotEmpty(newsSearchCriteriaModel.getAuthorDtoName())
                ? stringBuilder.append("author.name='" + newsSearchCriteriaModel.getAuthorDtoName() + "' ")
                : stringBuilder.append(MISSING);
    }

    private StringBuilder getAuthorSurnameQuery() {
        StringBuilder stringBuilder = new StringBuilder("and ");
        return isNotEmpty(newsSearchCriteriaModel.getAuthorDtoSurname())
                ? stringBuilder.append("author.surname='" +newsSearchCriteriaModel.getAuthorDtoSurname() + "' ")
                : stringBuilder.append(MISSING);
    }

    private StringBuilder getTagsQuery() {
        StringBuilder stringBuilder = new StringBuilder("and ");
        return !newsSearchCriteriaModel.getTags().isEmpty()
                ? stringBuilder.append("tag.name in (" + getSetOfTagsName(newsSearchCriteriaModel.getTags()) + ")")
                : stringBuilder.append(MISSING);
    }

    private StringBuilder getSetOfTagsName(List<String> list){
        StringBuilder stringBuilder = new StringBuilder();
        for (String name : list){
            stringBuilder.append("'" + name + "',");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        return stringBuilder;
    }

    private static boolean isNotEmpty(String str) {
        return str != null && !str.trim().equals("");
    }

}
