package com.epam.lab.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthorDto extends Dto {

    private Long id;
    private String name;
    private String surname;
    private List<NewsDto> newsList = new ArrayList<>();

    public AuthorDto() {
    }

    public AuthorDto(Long id) {
        this.id = id;
    }

    public AuthorDto(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public AuthorDto(Long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public AuthorDto(Long id, String name, String surname, List<NewsDto> newsList) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.newsList = newsList;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<NewsDto> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsDto> newsList) {
        this.newsList = newsList;
    }

    @Override
    public String toString() {
        return "AuthorDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", newsListSize=" + newsList.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDto authorDto = (AuthorDto) o;
        return Objects.equals(id, authorDto.id) &&
                Objects.equals(name, authorDto.name) &&
                Objects.equals(surname, authorDto.surname) &&
                Objects.equals(newsList, authorDto.newsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, newsList);
    }

}

