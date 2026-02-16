package io.securepath.authsphere.authserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
@RestController
@RequestMapping("/authorize")
public class Authorize {

    @GetMapping
    public ResponseEntity<?> authorize(
            @RequestParam String response_type,
            @RequestParam String client_id,
            @RequestParam String redirect_uri,
            @RequestParam String scope,
            @RequestParam(required = false) String state) {

        if (!"101".equals(client_id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid client_id");
        }

        // Instead of redirecting with GET, tell client to POST JSON to /authserver/login
        String message = "Please POST credentials to /authserver/login with JSON body: " +
                "{ \"userName\": \"prathmesh\", \"password\": \"secret\", \"redirectUri\": \"" + redirect_uri + "\" }";

        return ResponseEntity.ok(message);
    }

}
