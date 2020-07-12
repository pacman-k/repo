package com.dan.travel_agent.repository;

import com.dan.travel_agent.models.City;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CityRepository cityRepository;


    @Test
    public void saveAndReturnCity() {
        City testCity = new City();
        testCity.setCityName("Minsk");
        testCity.setCityDescription("It is the capital of Belarus");
        City savedCity = cityRepository.save(testCity);

        assertNotNull(savedCity);
        assertNotNull(savedCity.getCityId());
        assertEquals(testCity.getCityName(), savedCity.getCityName());
        assertEquals(testCity.getCityDescription(), savedCity.getCityDescription());
    }

    @Test
    public void findCityByID(){
        City testCity = new City();
        testCity.setCityName("Minsk");
        testCity.setCityDescription("It is the capital of Belarus");
        Object id = entityManager.persistAndGetId(testCity);

        assertTrue(cityRepository.findById((Long) id).isPresent());
    }

    @Test
    public void findCityByName(){
        City testCity = new City();
        testCity.setCityName("Minsk");
        testCity.setCityDescription("It is the capital of Belarus");
        entityManager.persistAndFlush(testCity);

        assertTrue(cityRepository.findByCityNameIgnoreCase(testCity.getCityName()).isPresent());
        assertTrue(cityRepository.findByCityNameIgnoreCase(testCity.getCityName().toUpperCase()).isPresent());
        assertTrue(cityRepository.findByCityNameIgnoreCase(testCity.getCityName().toLowerCase()).isPresent());
        assertFalse(cityRepository.findByCityNameIgnoreCase(testCity.getCityName() + " city").isPresent());
    }

    @Test
    public void findCityByTag(){
        String[] tags = {"минск", "mnsk"};
        City testCity = new City();
        testCity.setCityName("Minsk");
        testCity.setCityDescription("It is the capital of Belarus");
        testCity.setTags(new HashSet<>(Arrays.asList(tags)));
        entityManager.persistAndFlush(testCity);

        assertFalse(cityRepository.findByTagsIgnoreCase(tags[0]).isEmpty());
        assertFalse(cityRepository.findByTagsIgnoreCase(tags[1]).isEmpty());
    }
}