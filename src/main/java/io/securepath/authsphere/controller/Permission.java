package io.securepath.authsphere.controller;

import io.securepath.authsphere.response.ApiResponse;
import lombok.Getter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
@PreAuthorize("hasRole('ADMIN')")
public class Permission {

    @GetMapping("/getlist")
    public ApiResponse getPermissions(@RequestParam String login, @RequestParam String password) {
        ApiResponse lResponse = new ApiResponse();
        return  lResponse;
    }

    @PostMapping("/create")
    public ApiResponse createPermission(@RequestParam String login, @RequestParam String password) {
        ApiResponse lResponse = new ApiResponse();
        return  lResponse;
    }

    //here you can updated status of permission
    @PostMapping("/update")
    public ApiResponse updatePermission(@RequestParam String login, @RequestParam String password) {
        ApiResponse lResponse = new ApiResponse();
        return  lResponse;
    }

}
