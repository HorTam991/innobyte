package hu.tamas.airlines.service;

import hu.tamas.airlines.model.City;
import hu.tamas.airlines.repository.CityRepository;
import hu.tamas.airlines.util.SystemKeys;
import hu.tamas.airlines.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public City save(City city) {
        Optional<City> dbData = this.cityRepository.findByCityName(city.getCityName());
        if (!dbData.isPresent()) {
            return this.cityRepository.save(city);
        } else {
            return city;
        }
    }

    @Override
    public City update(City city) {
        return this.cityRepository.save(city);
    }

    @Override
    public List<City> findAll() {
        return this.cityRepository.findAll();
    }

    @Override
    public City findById(Long id) throws NotFoundException {
        Optional<City> result = this.cityRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new NotFoundException("City not found!");
        }
    }

    @Override
    public void deleteById(Long id) {
        this.cityRepository.deleteById(id);
    }

    @Override
    public void saveFromFileLine(String fileLine) {
        String[] lineArray = fileLine.split(SystemKeys.FILE_SEPARATOR);
        City city = new City();
        city.setCityName(lineArray[0]);
        city.setPopulation(Long.valueOf(lineArray[1]));
        this.cityRepository.save(city);
    }

    @Override
    public City findByCityName(String cityName) throws NotFoundException {
        Optional<City> result = this.cityRepository.findByCityName(cityName);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new NotFoundException("City not found!");
        }
    }

}
