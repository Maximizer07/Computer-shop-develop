package com.example.demo;

import lombok.*;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
public class User {
    String name;
    String LastName;
    String phone;
    String email;
    String password;
}
