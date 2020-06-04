package com.epam.lab.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewsSearchCriteriaDto extends Dto {

    private String authorDtoName ;
    private String authorDtoSurname ;
    private List<String> tags = new ArrayList<>();

    public NewsSearchCriteriaDto() {
    }


    public NewsSearchCriteriaDto(String authorDtoName, String authorDtoSurname, List<String> tags) {
        this.authorDtoName = authorDtoName;
        this.authorDtoSurname = authorDtoSurname;
        this.tags = tags;
    }

    public String getAuthorDtoName() {
        return authorDtoName;
    }

    public void setAuthorDtoName(String authorDtoName) {
        this.authorDtoName = authorDtoName;
    }

    public String getAuthorDtoSurname() {
        return authorDtoSurname;
    }

    public void setAuthorDtoSurname(String authorDtoSurname) {
        this.authorDtoSurname = authorDtoSurname;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "NewsSearchCriteriaDto{" +
                "authorDtoName='" + authorDtoName + '\'' +
                ", authorDtoSurname='" + authorDtoSurname + '\'' +
                ", tagList=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsSearchCriteriaDto that = (NewsSearchCriteriaDto) o;
        return Objects.equals(authorDtoName, that.authorDtoName) &&
                Objects.equals(authorDtoSurname, that.authorDtoSurname) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorDtoName, authorDtoSurname, tags);
    }
}
