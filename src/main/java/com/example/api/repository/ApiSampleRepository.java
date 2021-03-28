package com.example.api.repository;


import com.example.api.model.ApiSample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiSampleRepository extends JpaRepository<ApiSample, Long> {
}
