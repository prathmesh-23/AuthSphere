package io.securepath.authsphere.controller;

import io.securepath.authsphere.services.LoginService;
import io.securepath.authsphere.request.UserRequest;
import io.securepath.authsphere.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authserver")
public class Login {


    @Autowired
    private LoginService gLoginService;
    @PostMapping("/login")
    public ApiResponse login(@RequestBody UserRequest login) {
        ApiResponse lResponse = new ApiResponse();
        try {
            lResponse = gLoginService.loginProcess(login);
        } catch (Exception e) {
            System.out.println(e);
        }
        return lResponse;
    }
}