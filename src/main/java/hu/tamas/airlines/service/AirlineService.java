package hu.tamas.airlines.service;

import hu.tamas.airlines.model.Airline;
import hu.tamas.airlines.util.exception.NotFoundException;

import java.util.List;

public interface AirlineService {

    Airline save(Airline airline);

    Airline update(Airline airline);

    List<Airline> findAll();

    Airline findById(Long id) throws NotFoundException;

    void deleteById(Long id);

    Airline findByAirlineName(String airlineName) throws NotFoundException;

}
