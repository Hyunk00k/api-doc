package com.example.api.controller;

import com.example.api.dto.ApiDocumentDto;
import com.example.api.repository.ApiDocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/api-document")
public class ApiDocumentController {
  @Autowired ApiDocumentRepository apiDocumentRepository;

  @GetMapping("")
  public ResponseEntity<List<ApiDocumentDto>> findAll() {
    List<ApiDocumentDto> apiDocumentDtos = apiDocumentRepository.findAll();

    return new ResponseEntity<>(apiDocumentDtos, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiDocumentDto> findById(@PathVariable("id") long id) {
    Optional<ApiDocumentDto> apiDocuments = apiDocumentRepository.findById(id);

    if (apiDocuments.isPresent()) {
      return new ResponseEntity<>(apiDocuments.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/")
  public ResponseEntity<ApiDocumentDto> createApiDocument(
      @RequestBody ApiDocumentDto apiDocumentDto,
      HttpServletResponse httpResponse,
      WebRequest request) {
    ApiDocumentDto _apiDocumentDto = apiDocumentRepository.save(apiDocumentDto);

    httpResponse.setStatus(HttpStatus.CREATED.value());
    httpResponse.setHeader(
        "Location",
        String.format("%s/api/api-document/%s", request.getContextPath(), apiDocumentDto.getId()));

    return new ResponseEntity<>(_apiDocumentDto, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiDocumentDto> updateApiDocument(
      @PathVariable("id") long id, @RequestBody ApiDocumentDto apiDocumentDto) {
    Optional<ApiDocumentDto> apiDocumentData = apiDocumentRepository.findById(id);
    if (apiDocumentData.isPresent()) {
      ApiDocumentDto _apiDocumentDto = apiDocumentData.get();
      _apiDocumentDto.setTitle(apiDocumentDto.getTitle());
      _apiDocumentDto.setDescription(apiDocumentDto.getDescription());

      return new ResponseEntity<>(apiDocumentRepository.save(_apiDocumentDto), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiDocumentDto> deleteApiDocument(@PathVariable("id") long id) {
    try {
      apiDocumentRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>((HttpStatus.INTERNAL_SERVER_ERROR));
    }
  }
}
