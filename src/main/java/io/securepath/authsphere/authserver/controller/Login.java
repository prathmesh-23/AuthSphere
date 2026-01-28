package io.securepath.authsphere.authserver.controller;

import io.securepath.authsphere.authserver.config.JwtConfig;
import io.securepath.authsphere.request.UserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/authserver")
public class Login {

    private final JwtConfig jwtConfig;

    public Login(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest login) {
        if ("user".equals(login.getUserName()) && "password".equals(login.getPassword())) {
            String jwt = jwtConfig.generateToken(
                    login.getUserName(),
                    "123",          // userId
                    "USER",         // role
                    "127.0.0.1"     // ip
            );
            return ResponseEntity.ok(Map.of("access_token", jwt));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}