package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс веб-приложения
 * @author Maximus
 */
@SpringBootApplication
public class DemoApplication {
    /**
     * Входная точка веб-приложения
     * @param args Массив аргументов переданных при запуске веб-приложения
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
