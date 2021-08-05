package hu.tamas.airlines.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.tamas.airlines.model.Flight;
import hu.tamas.airlines.service.FlightService;
import hu.tamas.airlines.util.SystemKeys;
import hu.tamas.airlines.util.exception.DijkstraException;
import hu.tamas.airlines.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/flight")
@RequiredArgsConstructor
@Slf4j
public class FlightController {

    private final FlightService flightService;

    private final ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> findAll() {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.flightService.findAll()), HttpStatus.OK);
        } catch (IOException e) {
            log.error("findAll - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.flightService.findById(id)), HttpStatus.OK);
        } catch (IOException e) {
            log.error("findById - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> insert(@RequestBody Flight data) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.flightService.save(data)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("insert - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> update(@RequestBody Flight data) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.flightService.update(data)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("update - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> delete(@PathVariable Long id) {
        this.flightService.deleteById(id);
        return new ResponseEntity<>(SystemKeys.ResponseTexts.SUCCESS_DELETE, HttpStatus.OK);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, headers = SystemKeys.Spring.MULTIPART_CONTENT_TYPE)
    public ResponseEntity<String> uploadFlights(MultipartHttpServletRequest request) {
        try {
            log.info("Flight upload start...");
            File importFile = File.createTempFile("import_flights", UUID.randomUUID().toString());
            Files.write(importFile.toPath(), Objects.requireNonNull(request.getFile("import")).getBytes());
            List<String> allLines = Files.readAllLines(importFile.toPath());
            for (String line : allLines) {
                this.flightService.saveFromFileLine(line);
            }
            log.info("Flight upload end...");
            return new ResponseEntity<>(SystemKeys.ResponseTexts.SUCCESS_UPLOAD, HttpStatus.OK);
        } catch (IOException e) {
            log.error("uploadFlights - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.UPLOAD_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(SystemKeys.ResponseTexts.UPLOAD_FAILED_MISSING_FILE, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Deprecated
    @RequestMapping(value = "/resultOld", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> getRouteWithAllAirlinesOld(@RequestParam(value = "fromCity") final String fromCityName,
                                                             @RequestParam(value = "toCity") final String toCityName) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.flightService.getFlightRoute(null, fromCityName, toCityName)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("getRouteWithAllAirlines - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException | DijkstraException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> getRouteWithAllAirlines(@RequestParam(value = "fromCity") final String fromCityName,
                                                          @RequestParam(value = "toCity") final String toCityName) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.flightService.getFlightRoute(null, fromCityName, toCityName)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("getRouteWithAllAirlines - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException | DijkstraException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/result/{id}", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> getRouteWithOneAirlines(@PathVariable final Long id,
                                                          @RequestParam(value = "fromCity") final String fromCityName,
                                                          @RequestParam(value = "toCity") final String toCityName) {

        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.flightService.getFlightRoute(id, fromCityName, toCityName)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("getRouteWithOneAirlines - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException | DijkstraException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/way", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> waysBetweenCities(@RequestParam(value = "fromCity") final String fromCityName,
                                                    @RequestParam(value = "toCity") final String toCityName) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.flightService.findAllByFromCityIdAndToCityId(fromCityName, toCityName)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("waysBetweenCities - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
