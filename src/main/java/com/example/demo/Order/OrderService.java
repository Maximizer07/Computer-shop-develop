package com.example.demo.Order;

import com.example.demo.CartItem.CartItemService;
import com.example.demo.CartItem.Cart_Item;
import com.example.demo.Product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
@Transactional
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CartItemService cartItemService;

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
    public void delete(String number){
        for(Cart_Item cart_item: orderRepository.findByNumber(number).getProducts()){
            cartItemService.delete(cart_item);
        }
        orderRepository.deleteByNumber(number);
    }
}
