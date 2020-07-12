package com.dan.travel_agent.service;

import com.dan.travel_agent.models.City;
import com.dan.travel_agent.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {

    private CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City saveCity(City city) {
        return findCityByName(city.getCityName())
                .orElseGet(() -> cityRepository.save(city));
    }

    @Override
    public List<City> saveAll(List<City> cities) {
        return cities.stream()
                .map(this::saveCity)
                .collect(Collectors.toList());
    }

    @Override
    public City updateCity(City city) {
        return cityRepository.save(city);
    }

    @Override
    public Optional<City> deleteCity(Long id) {
        return getAndDeleteCity(id);
    }

    @Override
    public Optional<City> deleteCity(City city) {
        return getAndDeleteCity(city.getCityId());
    }

    private Optional<City> getAndDeleteCity(Long id) {
        Optional<City> city = cityRepository.findById(id);
        if (city.isPresent()) cityRepository.deleteById(id);
        return city;
    }

    @Override
    public Optional<City> findCityById(Long id) {
        return cityRepository.findById(id);
    }

    @Override
    public Optional<City> findCityByName(String name) {
        Optional<City> cityByName = cityRepository.findByCityNameIgnoreCase(name);
        return cityByName.isPresent()
                ? cityByName
                : cityRepository.findByTagsIgnoreCase(name).stream().findFirst();
    }

    @Override
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}
