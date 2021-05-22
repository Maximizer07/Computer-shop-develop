package com.example.demo.Property;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Integer> {
    Property findById(int id);
    Long deleteById(Long id);
}
