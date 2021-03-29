package com.example.api.repository;


import com.example.api.dto.ApiDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiDocumentRepository extends JpaRepository<ApiDocument, Long> {
}
