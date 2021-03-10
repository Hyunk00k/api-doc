package com.example.apisample.controller;

import com.example.apisample.model.ApiSample;
import com.example.apisample.repository.ApiSampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/api-sample")
public class ApiSampleController {
    @Autowired
    ApiSampleRepository apiSampleRepository;

    @GetMapping("")
    public ResponseEntity<List<ApiSample>> findAll() {
        List<ApiSample> apiSamples = apiSampleRepository.findAll();

        return new ResponseEntity<>(apiSamples, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiSample> findById(@PathVariable("id") long id) {
        Optional<ApiSample> apiSamples = apiSampleRepository.findById(id);

        if (apiSamples.isPresent()) {
            return new ResponseEntity<>(apiSamples.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<ApiSample> createApiSample(@RequestBody ApiSample apiSample, HttpServletResponse httpResponse,
                                                     WebRequest request) {
        ApiSample _apiSample
                = apiSampleRepository.save(apiSample);

        httpResponse.setStatus(HttpStatus.CREATED.value());
        httpResponse.setHeader("Location", String.format("%s/api/api-sample/%s",
                request.getContextPath(), apiSample.getId()));

        return new ResponseEntity<>(_apiSample, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiSample> updateApiSample(@PathVariable("id") long id, @RequestBody ApiSample apiSample) {
        Optional<ApiSample> apiSampleData = apiSampleRepository.findById(id);
        if (apiSampleData.isPresent()) {
            ApiSample _apiSample = apiSampleData.get();
            _apiSample.setTitle(apiSample.getTitle());
            _apiSample.setDescription(apiSample.getDescription());

            return new ResponseEntity<>(apiSampleRepository.save(_apiSample), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSample> deleteApiSample(@PathVariable("id") long id) {
        try {
            apiSampleRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>((HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}