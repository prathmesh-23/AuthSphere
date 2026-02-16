package io.securepath.authsphere.authserver.controller;

import io.securepath.authsphere.authserver.models.TokenRequest;
import io.securepath.authsphere.authserver.service.JWTToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authserver")
public class Token {

    private final JWTToken tokenService;

    public Token(JWTToken tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody TokenRequest request, HttpServletRequest httpRequest) {
        if (!"101".equals(request.getClient_id())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid client");
        }
        if (!"AUTH_CODE_123".equals(request.getCode())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid code");
        }

        String userIp = httpRequest.getRemoteAddr();
        String jwt = tokenService.generateToken("prathmesh", "123", "DEV", userIp);

        Map<String, Object> response = new HashMap<>();
        response.put("access_token", jwt);
        response.put("token_type", "Bearer");
        response.put("expires_in", 3600);

        return ResponseEntity.ok(response);
    }
}
