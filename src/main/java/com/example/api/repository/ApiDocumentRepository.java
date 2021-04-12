package com.example.api.repository;


import com.example.api.dto.ApiDocumentDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiDocumentRepository extends JpaRepository<ApiDocumentDto, Long> {

}
