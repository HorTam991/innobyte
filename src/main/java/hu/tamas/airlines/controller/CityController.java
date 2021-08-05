package hu.tamas.airlines.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.tamas.airlines.model.City;
import hu.tamas.airlines.service.CityService;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/city")
@RequiredArgsConstructor
@Slf4j
public class CityController {

    private final CityService cityService;

    private final ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> findAll() {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.cityService.findAll()), HttpStatus.OK);
        } catch (IOException e) {
            log.error("findAll - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.cityService.findById(id)), HttpStatus.OK);
        } catch (IOException e) {
            log.error("findById - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> insert(@RequestBody City data) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.cityService.save(data)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("insert - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> update(@RequestBody City data) {
        try {
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(this.cityService.update(data)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("update - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.ERROR_COMPILING_RESPONSE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = SystemKeys.Spring.PRODUCES_JSON_UTF8)
    public ResponseEntity<String> delete(@PathVariable Long id) {
        this.cityService.deleteById(id);
        return new ResponseEntity<>(SystemKeys.ResponseTexts.SUCCESS_DELETE, HttpStatus.OK);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, headers = SystemKeys.Spring.MULTIPART_CONTENT_TYPE)
    public ResponseEntity<String> uploadCities(MultipartHttpServletRequest request) {
        try {
            log.info("City upload start...");
            File importFile = File.createTempFile("import_cities", UUID.randomUUID().toString());
            Files.write(importFile.toPath(), Objects.requireNonNull(request.getFile("import")).getBytes());
            List<String> allLines = Files.readAllLines(importFile.toPath());
            for (String line : allLines) {
                this.cityService.saveFromFileLine(line);
            }
            log.info("City upload end...");
            return new ResponseEntity<>(SystemKeys.ResponseTexts.SUCCESS_UPLOAD, HttpStatus.OK);
        } catch (IOException e) {
            log.error("uploadCities - ", e);
            return new ResponseEntity<>(SystemKeys.ResponseTexts.UPLOAD_FAILED, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(SystemKeys.ResponseTexts.UPLOAD_FAILED_MISSING_FILE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
