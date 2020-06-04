package com.epam.lab.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TagDto extends Dto {

    private Long id;
    private String name;
    private List<NewsDto> newsList = new ArrayList<>();

    public TagDto() {
    }


    public TagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public TagDto(Long id, String name, List<NewsDto> newsList) {
        this.id = id;
        this.name = name;
        this.newsList = newsList;
    }

    public TagDto(Long id) {
        this.id = id;
    }

    public TagDto(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NewsDto> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsDto> newsList) {
        this.newsList = newsList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDto tagDto = (TagDto) o;
        return Objects.equals(id, tagDto.id) &&
                Objects.equals(name, tagDto.name) &&
                Objects.equals(newsList, tagDto.newsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, newsList);
    }

    @Override
    public String toString() {
        return "TagDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", newsListSize=" + newsList.size() +
                '}';
    }
}
