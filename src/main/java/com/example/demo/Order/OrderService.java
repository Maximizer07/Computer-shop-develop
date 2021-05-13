package com.example.demo.Order;

import com.example.demo.Product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public void save(Order o){
        orderRepository.save(o);
    }
    public List<Order> readAll() {
        return orderRepository.findAll();
    }
    public List<Order> findbyuser(long id){
        return orderRepository.findByUserid(id);
    }
    public Order findbynumber(String number){
        return orderRepository.findByNumber(number);
    }
}
