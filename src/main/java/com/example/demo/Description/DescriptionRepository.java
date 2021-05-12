package com.example.demo.Description;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DescriptionRepository extends JpaRepository<Description, Integer> {
    Description findById(int Id);
    Description findByProductid(int Id);
    Long deleteByProductid(int Id);
}
