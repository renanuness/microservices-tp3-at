package com.example.gateway;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class TokenFilter implements GlobalFilter, Ordered {

    private static final List<String> LIVRES = List.of(
            "/auth-service/login",
            "/auth-service/register"
    );

    private final SecretKey secretKey;

    public TokenFilter(@Value("${jwt.secret}") String key) {
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public int getOrder(){
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("Path: " + path);
        if(LIVRES.contains(path)){
            return chain.filter(exchange);
        }

        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if(header == null || !header.startsWith("Bearer ")){
            System.out.println("Token not present");
            return deny(exchange);
        }

        try{
            var token = header.substring(7);
            System.out.println("Token: " + token);
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        }catch (Exception e){
            System.out.println("Invalid token");
            System.out.println(e.getLocalizedMessage());
            return deny(exchange);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> deny(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
