package com.example.flattie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.flattie.model.PropertyManager;

@Repository
public interface PropertyManagerRepository extends JpaRepository<PropertyManager, Long> {
    
}