package hu.tamas.airlines.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.tamas.airlines.model.Airline;
import hu.tamas.airlines.service.AirlineService;
import hu.tamas.airlines.service.FlightService;
import hu.tamas.airlines.util.SystemKeys;
import hu.tamas.airlines.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
@RequestMapping("/airline")
@RequiredArgsConstructor
@Slf4j
public class AirlineController {

    private final AirlineService airlineService;

    private final FlightService flightService;

    private final ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> findAll() {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.airlineService.findAll()), HttpStatus.OK);
        } catch (IOException e) {
            log.error("findAll - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.airlineService.findById(id)), HttpStatus.OK);
        } catch (IOException e) {
            log.error("findById - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> insert(@RequestBody Airline data) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.airlineService.save(data)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("insert - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> update(@RequestBody Airline data) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.airlineService.update(data)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("update - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> delete(@PathVariable Long id) {
        this.airlineService.deleteById(id);
        return new ResponseEntity<>(SystemKeys.ResponseTexts.SUCCESS_DELETE, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/flight", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> getFlightsByAirline(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.flightService.findAllByAirlineId(id)), HttpStatus.OK);
        } catch (IOException e) {
            log.error("getFlightsByAirline - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
