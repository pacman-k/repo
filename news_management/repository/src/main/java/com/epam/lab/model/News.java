package com.epam.lab.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "news")
public class News extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "short_text", nullable = false)
    private String shortText;

    @Column(name = "full_text", nullable = false)
    private String fullText;

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column(name = "modification_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date modificationDate;

    @Transient
    private List<Long> tagIdList = new ArrayList<>();

    @Transient
    private Long authorId;

    @ManyToMany
    @JoinTable(
            name = "news_author",
            joinColumns = @JoinColumn(name = "news_id", referencedColumnName = "id", unique = true),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id")
    )
    private List<Author> authors = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "news_tag",
            joinColumns = @JoinColumn(name = "news_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    private List<Tag> tags = new ArrayList<>();


    public News() {
    }

    public News(String title, String shortText, String fullText) {
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
    }


    public News(String title, String shortText, String fullText, Date creationDate,
                Date modificationDate, List<Long> tagIdList, Long authorId) {
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.tagIdList = tagIdList;
        this.authorId = authorId;
    }

    public News(String title, String shortText, String fullText, Date creationDate, Date modificationDate, Long authorId) {
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.authorId = authorId;
    }


    @PostLoad
    private void postLoad(){
        authorId = authors.stream().map(Author::getId).findFirst().orElse(0L);
        tagIdList = tags.stream().map(Tag::getId).collect(Collectors.toList());
        title = title.trim();
        shortText = shortText.trim();
        fullText = fullText.trim();
    }


    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public List<Long> getTagIdList() {
        return tagIdList;
    }

    public void setTagIdList(List<Long> tagIdList) {
        this.tagIdList = tagIdList;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", shortText='" + shortText + '\'' +
                ", fullText='" + fullText + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                ", tagIdListSize=" + tagIdList.size() +
                ", authorId=" + authorId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(id, news.id) &&
                Objects.equals(title, news.title) &&
                Objects.equals(shortText, news.shortText) &&
                Objects.equals(fullText, news.fullText) &&
                Objects.equals(creationDate, news.creationDate) &&
                Objects.equals(modificationDate, news.modificationDate) &&
                Objects.equals(tagIdList, news.tagIdList) &&
                Objects.equals(authorId, news.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, shortText, fullText, creationDate, modificationDate, tagIdList, authorId);
    }
}
