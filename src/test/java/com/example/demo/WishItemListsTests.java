package com.example.demo;


import com.example.demo.WishItem.WishItem;
import com.example.demo.Product.Product;
import com.example.demo.WishItem.WishItemRepository;
import com.example.demo.WishItem.WishService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
public class WishItemListsTests {
    @Mock
    private WishItemRepository wishItemRepository;
    @Captor
    ArgumentCaptor<WishItem> captor;
    @Test
    void getWihsItems() {
        WishItem wishItem = new WishItem();
        Product product = new Product();
        product.setName("Процессор");
        product.setId(1);
        wishItem.setProduct(product);
        wishItem.setId(1L);
        WishItem wishItem2 = new WishItem();
        Product product2 = new Product();
        product2.setName("Жетский диск");
        product2.setId(2);
        wishItem2.setProduct(product);
        wishItem2.setId(2L);
        WishService wishService = new WishService(wishItemRepository);
        Mockito.when(wishItemRepository.findAll()).thenReturn(List.of(wishItem,wishItem2));
        assertEquals(2,
                wishService.findAll().size());
        assertEquals("Процессор",
                wishItemRepository.findAll().get(0).getProduct().getName());
    }
    @Test
    void WishItemExists() {
        WishItem wishItem = new WishItem();
        Product product = new Product();
        product.setName("Процессор");
        product.setId(1);
        wishItem.setProduct(product);
        wishItem.setId(1L);
        WishService wishService = new WishService(wishItemRepository);
        wishService.save(wishItem);
        Mockito.verify(wishItemRepository).save(captor.capture());
        WishItem captured = captor.getValue();
        assertEquals("Процессор", captured.getProduct().getName());
    }
    @Test
    void WishItemDelete() {
        WishItem wishItem = new WishItem();
        Product product = new Product();
        product.setName("Процессор");
        product.setId(1);
        wishItem.setProduct(product);
        wishItem.setId(1L);
        WishService wishService = new WishService(wishItemRepository);
        wishService.save(wishItem);
        Mockito.verify(wishItemRepository).save(captor.capture());
        WishItem captured = captor.getValue();
        assertEquals("Процессор", captured.getProduct().getName());
        wishService.delete(1L);
        Mockito.verify(wishItemRepository).deleteById(1L);
        assertEquals(0,wishService.findbyid(1L).size());
    }
}
