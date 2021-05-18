package com.example.demo.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);
    Category findById(int Id);
    Category findByEngname(String name);
    Long deleteById(String name);
    Long deleteByNameAndAndLink(String name, String Link);
}
