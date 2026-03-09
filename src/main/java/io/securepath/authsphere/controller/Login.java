package io.securepath.authsphere.controller;

import io.securepath.authsphere.services.LoginService;
import io.securepath.authsphere.request.UserRequest;
import io.securepath.authsphere.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authserver")
public class Login {

    private static final Logger glogger = LoggerFactory.getLogger(Login.class);

    @Autowired
    private LoginService gLoginService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody UserRequest login) {
        ApiResponse lResponse = new ApiResponse();
        try {
            lResponse = gLoginService.loginProcess(login);
        } catch (Exception e) {
            glogger.error("Exception in login", e);
        }
        return lResponse;
    }

    @PostMapping("/otpvalidate")
    public ApiResponse otpValidate(@RequestBody UserRequest pOtp) {
        ApiResponse lResponse = new ApiResponse();

        try {
            lResponse = gLoginService.otpValidate(pOtp);
        } catch (Exception e) {
            glogger.error("Exception in OTP Validate", e);

        }
        return lResponse;
    }

    @PostMapping("/forggotPassword")
    public ApiResponse forgotPassword(@RequestBody UserRequest pUserRequest) {
        ApiResponse lResponse = new ApiResponse();
        try {
            lResponse = gLoginService.forgotPassService(pUserRequest);
        } catch (Exception e) {
            glogger.error("Exception in Forgot Password", e);
        }
        return new ApiResponse();
    }

    @PostMapping("/changePassword")
    public ApiResponse changePassword(@RequestBody UserRequest pForgot) {
        ApiResponse lResponse = new ApiResponse();
        try {

        } catch (Exception e) {
            glogger.error("Exception in Change Password", e);
        }
        return new ApiResponse();
    }
}