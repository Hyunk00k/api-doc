package com.example.api.controller;

import com.example.api.dto.ApiDocument;
import com.example.api.repository.ApiDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/api-document")
public class ApiDocumentController {
    @Autowired
    ApiDocumentRepository apiDocumentRepository;

    @GetMapping("")
    public ResponseEntity<List<ApiDocument>> findAll() {
        List<ApiDocument> apiDocuments = apiDocumentRepository.findAll();

        return new ResponseEntity<>(apiDocuments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiDocument> findById(@PathVariable("id") long id) {
        Optional<ApiDocument> apiDocuments = apiDocumentRepository.findById(id);

        if (apiDocuments.isPresent()) {
            return new ResponseEntity<>(apiDocuments.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<ApiDocument> createApiDocument(@RequestBody ApiDocument apiDocument, HttpServletResponse httpResponse,
                                                       WebRequest request) {
        ApiDocument _apiDocument
                = apiDocumentRepository.save(apiDocument);

        httpResponse.setStatus(HttpStatus.CREATED.value());
        httpResponse.setHeader("Location", String.format("%s/api/api-document/%s",
                request.getContextPath(), apiDocument.getId()));

        return new ResponseEntity<>(_apiDocument, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDocument> updateApiDocument(@PathVariable("id") long id, @RequestBody ApiDocument apiDocument) {
        Optional<ApiDocument> apiDocumentData = apiDocumentRepository.findById(id);
        if (apiDocumentData.isPresent()) {
            ApiDocument _apiDocument = apiDocumentData.get();
            _apiDocument.setTitle(apiDocument.getTitle());
            _apiDocument.setDescription(apiDocument.getDescription());

            return new ResponseEntity<>(apiDocumentRepository.save(_apiDocument), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDocument> deleteApiDocument(@PathVariable("id") long id) {
        try {
            apiDocumentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>((HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}