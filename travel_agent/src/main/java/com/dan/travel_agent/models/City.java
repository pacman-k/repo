package com.dan.travel_agent.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.Set;

@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;

    @NotEmpty(message = "city name must not be empty")
    @Length(max = 30, message = "city name length must be not longer 30 characters")
    @Column(unique = true, nullable = false, length = 30)
    private String cityName;

    @NotEmpty(message = "city description must not be empty")
    @Length(max = 1000, message = "city description length must be not longer 1000 characters")
    @Column(nullable = false, length = 1000)
    private String cityDescription;

    @Valid
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "city_links", joinColumns = @JoinColumn(name = "city_id"))
    private Set<Link> links;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "city_tags", joinColumns = @JoinColumn(name = "city_id"))
    @Column(name = "tag")
    private Set<String> tags;

    public City() {
    }



    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityDescription() {
        return cityDescription;
    }

    public void setCityDescription(String cityDescription) {
        this.cityDescription = cityDescription;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(cityId, city.cityId) &&
                Objects.equals(cityName, city.cityName) &&
                Objects.equals(cityDescription, city.cityDescription) &&
                Objects.equals(links, city.links) &&
                Objects.equals(tags, city.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId, cityName, cityDescription, links, tags);
    }

    @Override
    public String toString() {
        return "City{" +
                "cityId=" + cityId +
                ", cityName='" + cityName + '\'' +
                ", cityDescription='" + cityDescription + '\'' +
                ", links=" + links +
                ", tags=" + tags +
                '}';
    }
}
