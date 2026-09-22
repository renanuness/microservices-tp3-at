package com.exemplo.authservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    private String name;
    private String email;
    private String password;



    public static User newUser(String name, String email, String password){
        var user = new User(name, email, password);
        return user;
    }

    private User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
