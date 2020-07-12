package com.dan.travel_agent.service;

import com.dan.travel_agent.models.City;

import java.util.List;
import java.util.Optional;

public interface CityService {
    City saveCity(City city);

    List<City> saveAll(List<City> cities);

    City updateCity(City city);

    Optional<City> deleteCity(Long id);

    Optional<City> deleteCity(City city);

    Optional<City> findCityById(Long id);

    Optional<City> findCityByName(String name);

    List<City> getAllCities();
}
