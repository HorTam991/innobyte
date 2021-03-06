package hu.tamas.airlines.service;

import hu.tamas.airlines.model.Flight;
import hu.tamas.airlines.util.exception.DijkstraException;
import hu.tamas.airlines.util.exception.NotFoundException;

import java.util.LinkedList;
import java.util.List;

public interface FlightService {

    Flight save(Flight flight);

    Flight update(Flight flight);

    List<Flight> findAll();

    Flight findById(Long id) throws NotFoundException;

    void deleteById(Long id);

    void saveFromFileLine(String fileLine) throws NotFoundException;

    List<Flight> findAllByAirlineId(Long airlineId);

    LinkedList<Flight> getFlightRoute(Long airlineId, Long fromCityId, Long toCityId) throws NotFoundException, DijkstraException;

    List<Flight> findAllByFromCityIdAndToCityId(Long fromCityId, Long  toCityId) throws NotFoundException;
    
}
