package com.example.demo;

import com.example.demo.User.User;
import com.example.demo.User.UserRepository;
import com.example.demo.User.UserService;
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
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Captor
    ArgumentCaptor<User> captor;
    @Test
    void getUsers() {
        User user = new User();
        user.setName("Vasya");
        User user2 = new User();
        user2.setName("Dima");
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user,
                user2));
        UserService us =new UserService(userRepository);
        assertEquals(2,
                us.readAll().size());
        assertEquals("Vasya",
                userRepository.findAll().get(0).getName());
    }
    @Test
    void userExists() {
        User user = new User();
        user.setName("Vasya");
        user.setEmail("vasia@gmail.com");
        user.setId(1L);
        user.setPassword("123456");
        UserService us =new UserService(userRepository);
        Mockito.when(userRepository.findByEmail("vasia@gmail.com")).thenReturn(java.util.Optional.of(user));
        assertEquals("Vasya",
                us.loadUserByUsername("vasia@gmail.com").getName());
    }
}
