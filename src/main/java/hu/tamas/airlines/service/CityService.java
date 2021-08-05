package hu.tamas.airlines.service;

import hu.tamas.airlines.model.City;
import hu.tamas.airlines.util.exception.NotFoundException;

import java.util.List;

public interface CityService {

    City save(City city);

    City update(City city);

    List<City> findAll();

    City findById(Long id) throws NotFoundException;

    void deleteById(Long id);

    void saveFromFileLine(String fileLine);

    City findByCityName(String cityName) throws NotFoundException;

}
