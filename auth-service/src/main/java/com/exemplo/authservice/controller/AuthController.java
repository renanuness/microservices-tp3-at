package com.exemplo.authservice.controller;

import com.exemplo.authservice.dto.LoginDto;
import com.exemplo.authservice.dto.LoginResponseDto;
import com.exemplo.authservice.dto.RefreshTokenDto;
import com.exemplo.authservice.dto.RegisterDto;
import com.exemplo.authservice.service.AuthService;
import com.exemplo.authservice.service.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private final AuthService service;
    private final JwtToken tokenService;

    public AuthController(AuthService service, JwtToken tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDto dto){
        service.register(dto);
        return ResponseEntity.created(URI.create("")).build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto dto){
        var user = service.login(dto);

        var token = tokenService.generateToken(user);
        var refreshToken = tokenService.generateRefreshToken(user);

        return ResponseEntity.ok(new LoginResponseDto(token, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto request) {
        String refreshToken = request.refreshToken();
        if (tokenService.validateToken(refreshToken)) {
            String email = tokenService.getEmailFromToken(refreshToken);
            var user = service.getUserByEmail(email);
            String newAccessToken = tokenService.generateToken(user);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            return ResponseEntity.ok(tokens);
        }
        return ResponseEntity.status(403).body("Invalid refresh token");
    }
}
