package io.securepath.authsphere.authserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallbackController {
    @GetMapping("/callback")
    public String callback(@RequestParam String code, @RequestParam(required = false) String state) {
        return "Received code: " + code + " and state: " + state;
    }
}
