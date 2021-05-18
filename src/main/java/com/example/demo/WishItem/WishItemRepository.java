package com.example.demo.WishItem;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishItemRepository extends JpaRepository<WishItem, Long> {
    List<WishItem> findById(long id);
}