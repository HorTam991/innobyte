package hu.tamas.airlines.service;

import hu.tamas.airlines.model.Airline;
import hu.tamas.airlines.model.City;
import hu.tamas.airlines.model.Flight;
import hu.tamas.airlines.model.FlightGraph;
import hu.tamas.airlines.repository.AirlineRepository;
import hu.tamas.airlines.repository.CityRepository;
import hu.tamas.airlines.repository.FlightRepository;
import hu.tamas.airlines.util.Dijkstra;
import hu.tamas.airlines.util.DijkstraV2;
import hu.tamas.airlines.util.SystemKeys;
import hu.tamas.airlines.util.exception.DijkstraException;
import hu.tamas.airlines.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    private final AirlineRepository airlineRepository;

    private final CityRepository cityRepository;

    @Override
    public Flight save(Flight flight) {
        return this.flightRepository.save(flight);
    }

    @Override
    public Flight update(Flight flight) {
        return this.flightRepository.save(flight);
    }

    @Override
    public List<Flight> findAll() {
        return this.flightRepository.findAll();
    }

    @Override
    public Flight findById(Long id) throws NotFoundException {
        Optional<Flight> result = this.flightRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new NotFoundException("Flight not found!");
        }
    }

    @Override
    public void deleteById(Long id) {
        this.flightRepository.deleteById(id);
    }

    @Override
    public void saveFromFileLine(String fileLine) throws NotFoundException {
        String[] lineArray = fileLine.split(SystemKeys.FILE_SEPARATOR);
        Optional<Airline> airline = this.airlineRepository.findByAirlineName(lineArray[0]);
        Airline saveAirline = new Airline();
        if (!airline.isPresent()) {
            saveAirline.setAirlineName(lineArray[0]);
            saveAirline = this.airlineRepository.save(saveAirline);
        }
        Optional<City> fromCity = this.cityRepository.findByCityName(lineArray[1]);
        if (!fromCity.isPresent()) {
            throw new NotFoundException("City (" + lineArray[1] + ") not found!");
        }
        Optional<City> toCity = this.cityRepository.findByCityName(lineArray[2]);
        if (!toCity.isPresent()) {
            throw new NotFoundException("City (" + lineArray[2] + ") not found!");
        }
        Flight flight = new Flight();
        flight.setAirlineId(airline.orElse(saveAirline).getId());
        flight.setFromCityId(fromCity.get().getId());
        flight.setToCityId(toCity.get().getId());
        flight.setDistance(Integer.valueOf(lineArray[3]));
        flight.setFlightTime(Long.valueOf(lineArray[4]));
        this.flightRepository.save(flight);
    }

    @Override
    public List<Flight> findAllByAirlineId(Long airlineId) {
        return this.flightRepository.findAllByAirlineId(airlineId);
    }

    @Override
    public LinkedList<City> getFlightRouteCities(Long airlineId, String fromCityName, String toCityName) throws NotFoundException, DijkstraException {
        if (fromCityName.equals(toCityName)) {
            throw new DijkstraException(SystemKeys.ResponseTexts.SAME_CITIES_ERROR);
        }
        Optional<City> source = this.cityRepository.findByCityName(fromCityName);
        Optional<City> destination = this.cityRepository.findByCityName(toCityName);
        if (!source.isPresent()) {
            throw new NotFoundException("Source city (" + fromCityName + ") not found!");
        }
        if (!destination.isPresent()) {
            throw new NotFoundException("Destination city (" + toCityName + ") not found!");
        }

        List<Flight> edges;
        if (airlineId != null) {
            Optional<Airline> airline = this.airlineRepository.findById(airlineId);
            if (!airline.isPresent()) {
                throw new NotFoundException("Airline (" + airlineId + ") not found!");
            }
            edges = this.flightRepository.findAllByAirlineId(airline.get().getId());
        } else {
            edges = this.flightRepository.findAll();
        }

        FlightGraph graph = new FlightGraph(edges);
        Dijkstra dijkstra = new Dijkstra(graph);
        dijkstra.execute(source.get());
        return dijkstra.getPath(destination.get());
    }

    @Override
    public LinkedList<Flight> getFlightRoute(Long airlineId, String fromCityName, String toCityName) throws NotFoundException, DijkstraException {
        if (fromCityName.equals(toCityName)) {
            throw new DijkstraException(SystemKeys.ResponseTexts.SAME_CITIES_ERROR);
        }
        Optional<City> source = this.cityRepository.findByCityName(fromCityName);
        Optional<City> destination = this.cityRepository.findByCityName(toCityName);
        if (!source.isPresent()) {
            throw new NotFoundException("Source city (" + fromCityName + ") not found!");
        }
        if (!destination.isPresent()) {
            throw new NotFoundException("Destination city (" + toCityName + ") not found!");
        }

        List<Flight> edges;
        if (airlineId != null) {
            Optional<Airline> airline = this.airlineRepository.findById(airlineId);
            if (!airline.isPresent()) {
                throw new NotFoundException("Airline (" + airlineId + ") not found!");
            }
            edges = this.flightRepository.findAllByAirlineId(airline.get().getId());
        } else {
            edges = this.flightRepository.findAll();
        }

        FlightGraph graph = new FlightGraph(edges);
        DijkstraV2 dijkstra = new DijkstraV2(graph);
        dijkstra.execute(source.get());
        return dijkstra.getPath(source.get(), destination.get());
    }

    @Override
    public List<Flight> findAllByFromCityIdAndToCityId(String fromCityName, String  toCityName) throws NotFoundException {
        Optional<City> source = this.cityRepository.findByCityName(fromCityName);
        Optional<City> destination = this.cityRepository.findByCityName(toCityName);
        if (!source.isPresent()) {
            throw new NotFoundException("Source city (" + fromCityName + ") not found!");
        }
        if (!destination.isPresent()) {
            throw new NotFoundException("Destination city (" + toCityName + ") not found!");
        }
        return this.flightRepository.findAllByFromCityIdAndToCityId(source.get().getId(), destination.get().getId());
    }

}
