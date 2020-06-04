package com.epam.lab.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;


@Data
@AllArgsConstructor
@Builder
@JsonAutoDetect
@Entity
@Table(name = "news")
public class News {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "title", nullable = false)
    private String title;

    @NotEmpty
    @Column(name = "short_text", nullable = false)
    private String shortText;

    @NotEmpty
    @Column(name = "full_text", nullable = false)
    private String fullText;

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonIgnore
    private Date creationDate;

    @Column(name = "modification_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonIgnore
    private Date modificationDate;


    public News() {
        this.creationDate = new Date();
        this.modificationDate = new Date();
    }

}
