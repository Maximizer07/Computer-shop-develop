package com.example.demo.WishItem;

import com.example.demo.Order.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishService {
    final
    WishItemRepository wishItemRepository;

    public WishService(WishItemRepository wishItemRepository) {
        this.wishItemRepository = wishItemRepository;
    }
    public List<WishItem> findAll(){
        return wishItemRepository.findAll();
    }
    public void save(WishItem wishItem){
        wishItemRepository.save(wishItem);
    }
    public void delete(Long id){
        wishItemRepository.deleteById(id);
    }
}
