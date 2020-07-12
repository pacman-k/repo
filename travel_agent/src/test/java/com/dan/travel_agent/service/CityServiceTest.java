package com.dan.travel_agent.service;

import com.dan.travel_agent.models.City;
import com.dan.travel_agent.repository.CityRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class CityServiceTest {

    @TestConfiguration
    static class ServiceTestConfiguration {
        @Bean
        public List<City> getCities() {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(
                        ResourceUtils.getFile("classpath:cities.json"),
                        new TypeReference<List<City>>() {});
            } catch (IOException e) {
                return Collections.emptyList();
            }
        }

        @Bean
        public CityRepository cityRepository() {
            return Mockito.mock(CityRepository.class);
        }

        @Bean
        public CityService cityService(CityRepository cityRepository) {
            return new CityServiceImpl(cityRepository);
        }
    }

    @Autowired
    private CityService cityService;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private List<City> cities;

    @Before
    public void setUpRepository() {
        ObjectMapper objectMapper = new ObjectMapper();

        Mockito.when(cityRepository.findByCityNameIgnoreCase(Mockito.anyString()))
                .thenAnswer(invocationOnMock -> {
                    String cityName = invocationOnMock.getArgument(0);
                    return cities.stream()
                            .filter(city -> city.getCityName().equalsIgnoreCase(cityName))
                            .findFirst();
                });
        Mockito.when(cityRepository.findByTagsIgnoreCase(Mockito.anyString()))
                .thenAnswer(invocationOnMock -> {
                    String tag = invocationOnMock.getArgument(0);
                    return cities.stream()
                            .filter(city -> city.getTags().stream()
                                    .anyMatch(tagName -> tagName.equalsIgnoreCase(tag)))
                            .collect(Collectors.toSet());
                });
        Mockito.when(cityRepository.findById(Mockito.anyLong()))
                .thenAnswer(invocationOnMock -> {
                    Long id = invocationOnMock.getArgument(0);
                    return cities.stream()
                            .filter(city -> city.getCityId().equals(id))
                            .findFirst();
                });
        Mockito.when(cityRepository.findAll()).thenReturn(cities);
    }

    @Test
    public void findCityByName() {
        assertTrue(cityService.findCityByName("moscow").isPresent());
        assertTrue(cityService.findCityByName("mosc").isPresent());
        assertTrue(cityService.findCityByName("Tokyo").isPresent());
        assertTrue(cityService.findCityByName("ньюёрк").isPresent());
        assertFalse(cityService.findCityByName("ньюёsgfhsuygfhрк").isPresent());
    }

    @Test
    public void deleteAndGetCity() {
        assertTrue(cityService.deleteCity(1L).isPresent());
        assertTrue(cityService.deleteCity(2L).isPresent());
        assertTrue(cityService.deleteCity(3L).isPresent());
        assertFalse(cityService.deleteCity(376254L).isPresent());
    }

    @Test
    public void findAllCities(){
        assertEquals(3, cityService.getAllCities().size());
    }

}
