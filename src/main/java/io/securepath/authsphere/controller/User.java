package io.securepath.authsphere.controller;

import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.request.UserRequest;
import io.securepath.authsphere.response.ApiResponse;
import io.securepath.authsphere.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class User {

    private static final Logger gloggerog = LoggerFactory.getLogger(User.class);
    @Autowired
    private UserService gUserService;

    @PostMapping("/getUsers")
    public ApiResponse getUsers(@RequestBody UserRequest pUserReq) {
        gloggerog.info("Get User API CALL{}", pUserReq);
        ApiResponse lAPIResponse = new ApiResponse();
        try {
            lAPIResponse = gUserService.getUsersList(pUserReq);
        } catch (Exception e) {
            gloggerog.error("Exception:"+ e);
            lAPIResponse.setResponse("Error IN API");
            lAPIResponse.setStatus("BAD");
        }
        gloggerog.info("Get User Response{}", lAPIResponse);
        return lAPIResponse;
    }

    @PostMapping("/getUser")
    public ApiResponse getUserByID(@RequestBody UserRequest pUserReq) {
        gloggerog.info("Get User By ID API CALL : {}", pUserReq);
        ApiResponse lAPIResponse = new ApiResponse();
        try {
            lAPIResponse = gUserService.getUserDetails(pUserReq);
        } catch (Exception e) {
            gloggerog.error("Exception"+ e);
            lAPIResponse.setResponse("Error IN API");
            lAPIResponse.setStatus("BAD");
        }
        gloggerog.info("Get User Response{}", lAPIResponse);
        return lAPIResponse;

    }

    @PostMapping("/createUser")
    public ApiResponse createUser(@RequestBody UserRequest pUser) {
        gloggerog.info("Create User API CALL{}", pUser);
        ApiResponse lAPIResponse = new ApiResponse();
        try {
            lAPIResponse = gUserService.createUser(pUser);
        } catch (Exception e) {
            gloggerog.error("Exception"+ e);
            lAPIResponse.setResponse("Error IN API");
            lAPIResponse.setStatus("BAD");
        }
        gloggerog.info("Create User Response{}", lAPIResponse);
        return lAPIResponse;

    }

    @PostMapping("/Update")
    public ApiResponse updateUser(@RequestBody UserRequest pUser){
        gloggerog.info("Update User API CALL{}", pUser);
        ApiResponse lAPIResponse = new ApiResponse();
        try {
            lAPIResponse =  gUserService.updateUser(pUser);
        } catch (Exception e) {
            gloggerog.error("Exception"+ e);
            lAPIResponse.setResponse("Error IN API");
            lAPIResponse.setStatus("BAD");
        }
        gloggerog.info("Update User Response{}", lAPIResponse);
        return lAPIResponse;

    }


}
