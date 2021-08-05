package hu.tamas.airlines.repository;

import hu.tamas.airlines.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findAllByAirlineId(Long airlineId);

    List<Flight> findAllByFromCityIdAndToCityId(Long fromCityId, Long toCityId);

}
