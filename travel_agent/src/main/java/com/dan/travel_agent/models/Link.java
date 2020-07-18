package com.dan.travel_agent.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Embeddable
public class Link {

    @NotEmpty(message = "link must not be empty")
    @Pattern(regexp = "(https?://)?(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)",
            message = "invalid link pattern")
    @Column(nullable = false)
    private String link;

    @NotEmpty(message = "link description must not be empty")
    @Length(max = 70, message = "link description length must be not longer 40 characters")
    @Column(nullable = false, length = 70)
    private String linkDescription;

    public Link() {
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkDescription() {
        return linkDescription;
    }

    public void setLinkDescription(String linkDescription) {
        this.linkDescription = linkDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link1 = (Link) o;
        return Objects.equals(link, link1.link) &&
                Objects.equals(linkDescription, link1.linkDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, linkDescription);
    }

    @Override
    public String toString() {
        return "Link{" +
                ", link='" + link + '\'' +
                ", description='" + linkDescription + '\'' +
                '}';
    }
}
