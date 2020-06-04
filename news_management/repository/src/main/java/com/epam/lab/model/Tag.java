package com.epam.lab.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "tag")
public class Tag extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @NotEmpty(message = "tag name must be input")
    private String name;

    @Transient
    private List<Long> newsIdList = new ArrayList<>();

    @ManyToMany(mappedBy = "tags")
    private List<News> newsList = new ArrayList<>();



    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(Long id) {
        this.id = id;
    }

    @PostLoad
    private void postLoad() {
        newsIdList = newsList.stream().map(News::getId).collect(Collectors.toList());
        name = name.trim();
    }


    public List<Long> getNewsIdList() {
        return newsIdList;
    }

    public void setNewsIdList(List<Long> newsIdList) {
        this.newsIdList = newsIdList;
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

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", newsIdListSize=" + newsIdList.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) &&
                Objects.equals(name, tag.name) &&
                Objects.equals(newsIdList, tag.newsIdList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, newsIdList);
    }
}
