package com.example.demo;
import com.example.demo.CartItem.CartItemService;
import com.example.demo.CartItem.Cart_Item;
import com.example.demo.Order.Order;
import com.example.demo.Order.OrderRepository;
import com.example.demo.Order.OrderService;
import com.example.demo.Product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {
    @Mock
    private OrderRepository orderRepository;
    @Captor
    ArgumentCaptor<Order> captor;
    @Test
    void getOrders() {
        Order o = new Order();
        o.setNumber("123456");
        o.setStatus(0);
        o.setPrise(500);
        o.setUserid(1L);
        Order o2 = new Order();
        o2.setNumber("987654");
        o2.setStatus(2);
        o2.setPrise(1000);
        o2.setUserid(2L);
        Mockito.when(orderRepository.findAll()).thenReturn(List.of(o,o2));
        OrderService orderService = new OrderService(orderRepository);
        assertEquals(2,
                orderService.readAll().size());
        assertEquals("987654",
                orderService.readAll().get(1).getNumber());
    }
    @Test
    void OrderExists() {
        Order o = new Order();
        o.setNumber("123456");
        o.setStatus(0);
        o.setPrise(500);
        o.setUserid(1L);
        OrderService orderService = new OrderService(orderRepository);
        orderService.save(o);
        Mockito.verify(orderRepository).save(captor.capture());
        Order captured = captor.getValue();
        assertEquals(0, captured.getStatus());
    }
    @Test
    void OrderDelete() {
        Order o = new Order();
        o.setNumber("123456");
        o.setStatus(0);
        o.setPrise(500);
        o.setUserid(1L);
        OrderService orderService = new OrderService(orderRepository);
        orderService.save(o);
        Mockito.verify(orderRepository).save(captor.capture());
        Order captured = captor.getValue();
        assertEquals(0, captured.getStatus());
        Mockito.when(orderRepository.findByNumber(o.getNumber())).thenReturn(o);
        orderService.delete(o.getNumber());
        Mockito.verify(orderRepository).deleteByNumber(o.getNumber());
        assertEquals(1L,orderService.findbynumber(o.getNumber()).getUserid());
    }
}
