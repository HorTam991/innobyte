package hu.tamas.airlines.service;

import hu.tamas.airlines.model.Airline;
import hu.tamas.airlines.repository.AirlineRepository;
import hu.tamas.airlines.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;

    @Override
    public Airline save(Airline airline) {
        Optional<Airline> dbData = this.airlineRepository.findByAirlineName(airline.getAirlineName());
        if (!dbData.isPresent()) {
            return this.airlineRepository.save(airline);
        } else {
            return airline;
        }
    }

    @Override
    public Airline update(Airline airline) {
        return this.airlineRepository.save(airline);
    }

    @Override
    public List<Airline> findAll() {
        return this.airlineRepository.findAll();
    }

    @Override
    public Airline findById(Long id) throws NotFoundException {
        Optional<Airline> result = this.airlineRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new NotFoundException("Airline not found!");
        }
    }

    @Override
    public void deleteById(Long id) {
        this.airlineRepository.deleteById(id);
    }

    @Override
    public Airline findByAirlineName(String airlineName) throws NotFoundException {
        Optional<Airline> result = this.airlineRepository.findByAirlineName(airlineName);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new NotFoundException("Airline not found!");
        }
    }

}
