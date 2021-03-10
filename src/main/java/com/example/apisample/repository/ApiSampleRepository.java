package com.example.apisample.repository;


import com.example.apisample.model.ApiSample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiSampleRepository extends JpaRepository<ApiSample, Long> {
}
