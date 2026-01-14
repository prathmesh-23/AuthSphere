package io.securepath.authsphere.controller;

import io.securepath.authsphere.request.UserRequest;
import io.securepath.authsphere.response.ApiResponse;
import io.securepath.authsphere.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class User {

    @Autowired
    private UserService gUserService;

    @PostMapping("/getUsers")
    public ApiResponse getUser(@RequestBody UserRequest pUserReq){
        return  gUserService.getUserDetails(pUserReq);
    }
}
