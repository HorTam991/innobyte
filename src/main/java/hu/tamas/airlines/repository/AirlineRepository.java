package hu.tamas.airlines.repository;

import hu.tamas.airlines.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline, Long> {

    Optional<Airline> findByAirlineName(String airlineName);

}
