package com.exemplo.authservice.service;

import com.exemplo.authservice.dto.LoginDto;
import com.exemplo.authservice.dto.RegisterDto;
import com.exemplo.authservice.model.User;
import com.exemplo.authservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void register(RegisterDto dto){
        var encodedPassword = passwordEncoder.encode(dto.password());

        var newUser = User.newUser(dto.name(), dto.email(), encodedPassword);

        userRepository.save(newUser);
    }

    public User login(LoginDto dto){
        User user = userRepository.findByEmail(dto.email()).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos"));

        if(!passwordEncoder.matches(dto.password(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }

        return user;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos"));

        return user;
    }
}
