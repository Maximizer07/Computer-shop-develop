package com.example.demo;

import com.example.demo.CartItem.CartItemRepository;
import com.example.demo.CartItem.CartItemService;
import com.example.demo.CartItem.Cart_Item;
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
public class CartItemServiceTests {
    @Mock
    private CartItemRepository cartItemRepository;
    @Captor
    ArgumentCaptor<Cart_Item> captor;
    @Test
    void getCartItems() {
        Cart_Item cart_item = new Cart_Item();
        cart_item.setQuantity(1);
        Product product = new Product();
        product.setName("Процессор");
        cart_item.setProduct(product);
        Cart_Item cart_item2 = new Cart_Item();
        Product product2 = new Product();
        product2.setName("Жетский диск");
        cart_item2.setProduct(product2);
        cart_item2.setQuantity(1);
        Mockito.when(cartItemRepository.findAll()).thenReturn(List.of(cart_item,cart_item2));
        CartItemService cartItemService = new CartItemService(cartItemRepository);
        assertEquals(2,
                cartItemService.readAll().size());
        assertEquals("Процессор",
                cartItemRepository.findAll().get(0).getProduct().getName());
    }
    @Test
    void CartItemExists() {
        Cart_Item cart_item = new Cart_Item();
        cart_item.setQuantity(1);
        Product product = new Product();
        product.setName("Процессор");
        product.setId(1);
        cart_item.setProduct(product);
        CartItemService cartItemService = new CartItemService(cartItemRepository);
        cartItemService.create(cart_item);
        Mockito.verify(cartItemRepository).save(captor.capture());
        Cart_Item captured = captor.getValue();
        assertEquals("Процессор", captured.getProduct().getName());
    }
    @Test
    void CartItemDelete() {
        Cart_Item cart_item = new Cart_Item();
        cart_item.setQuantity(1);
        Product product = new Product();
        product.setName("Процессор");
        product.setId(1);
        cart_item.setProduct(product);
        CartItemService cartItemService = new CartItemService(cartItemRepository);
        cartItemService.create(cart_item);
        Mockito.verify(cartItemRepository).save(captor.capture());
        Cart_Item captured = captor.getValue();
        assertEquals("Процессор", captured.getProduct().getName());
        cartItemService.delete(cart_item);
        Mockito.verify(cartItemRepository).delete(captor.capture());
        assertTrue(cartItemService.findbyid(cart_item.getProduct().getId()).isEmpty());
    }
}

