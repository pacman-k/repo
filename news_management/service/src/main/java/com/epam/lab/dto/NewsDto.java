package com.epam.lab.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewsDto extends Dto {

    private Long id;
    private String title;
    private String shortText;
    private String fullText;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate modificationDate;
    private List<TagDto> tagList = new ArrayList<>();
    private AuthorDto authorDto;

    public NewsDto() {
    }

    public NewsDto(Long id) {
        this.id = id;
    }

    public NewsDto(Long id, String title, String shortText, String fullText) {
        this.id = id;
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
    }

    public NewsDto(Long id, String title, String shortText, String fullText, List<TagDto> tagList) {
        this.id = id;
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
        this.tagList = tagList;
    }
    public NewsDto(Long id, String title, String shortText, String fullText, List<TagDto> tagList, AuthorDto authorDto) {
        this.id = id;
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
        this.tagList = tagList;
        this.authorDto = authorDto;
    }

    public NewsDto(Long id, List<TagDto> tagList) {
        this.id = id;
        this.tagList = tagList;
    }


    public NewsDto(String title, String shortText, String fullText, AuthorDto authorDto) {
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
        this.authorDto = authorDto;
    }

    public NewsDto(String title, String shortText, String fullText, List<TagDto> tagList, AuthorDto authorDto) {
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
        this.tagList = tagList;
        this.authorDto = authorDto;
    }

    public AuthorDto getAuthorDto() {
        return authorDto;
    }

    public void setAuthorDto(AuthorDto authorDto) {
        this.authorDto = authorDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }


    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDate modificationDate) {
        this.modificationDate = modificationDate;
    }

    public List<TagDto> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagDto> tagList) {
        this.tagList = tagList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsDto newsDto = (NewsDto) o;
        return Objects.equals(id, newsDto.id) &&
                Objects.equals(title, newsDto.title) &&
                Objects.equals(shortText, newsDto.shortText) &&
                Objects.equals(fullText, newsDto.fullText) &&
                Objects.equals(creationDate, newsDto.creationDate) &&
                Objects.equals(modificationDate, newsDto.modificationDate) &&
                Objects.equals(tagList, newsDto.tagList) &&
                Objects.equals(authorDto, newsDto.authorDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, shortText, fullText, creationDate, modificationDate, tagList, authorDto);
    }

    @Override
    public String toString() {
        return "NewsDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", shortText='" + shortText + '\'' +
                ", fullText='" + fullText + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", tagListSize=" + tagList.size() +
                ", authorDto=" + authorDto +
                '}';
    }

}
