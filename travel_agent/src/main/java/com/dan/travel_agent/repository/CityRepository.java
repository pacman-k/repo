package com.dan.travel_agent.repository;

import com.dan.travel_agent.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByCityNameIgnoreCase(String cityName);

    Set<City> findByTagsIgnoreCase(String tag);
}
