package com.dan.travel_agent.controller;

import com.dan.travel_agent.models.City;
import com.dan.travel_agent.service.CheckId;
import com.dan.travel_agent.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cities")
@Validated
public class CityController {
    private CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    public ResponseEntity<City> saveCity(@RequestBody @Valid City city) {
        return ResponseEntity.ok(cityService.saveCity(city));
    }

    @PostMapping(path = "/addAll")
    public ResponseEntity<List<City>> saveAllCities(@RequestBody @Valid List<City> cities) {
        return ResponseEntity.ok(cityService.saveAll(cities));
    }

    @PutMapping
    public ResponseEntity<City> updateCity(@RequestBody @CheckId(checkIfExist = true) @Valid City city) {
        return ResponseEntity.ok(cityService.updateCity(city));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<City> deleteCity(@PathVariable Long id) {
        Optional<City> city = cityService.deleteCity(id);
        return ResponseEntity.of(city);
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        Optional<City> cityById = cityService.findCityById(id);
        return ResponseEntity.of(cityById);
    }

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    @GetMapping("/city/{name}")
    public ResponseEntity<City> getCityByName(@PathVariable String name) {
        Optional<City> cityByName = cityService.findCityByName(name);
        return ResponseEntity.of(cityByName);
    }


}
