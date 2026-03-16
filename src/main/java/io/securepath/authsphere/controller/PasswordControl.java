package io.securepath.authsphere.controller;


import io.securepath.authsphere.response.ApiResponse;
import io.securepath.authsphere.services.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passwordpolicy")
public class PasswordControl {
    private static final Logger glogger = LoggerFactory.getLogger(PasswordControl.class);

    @Autowired
    private PasswordService gPasswordService;

    @GetMapping("rest/password")
    public ApiResponse restpassword(@Param("key") String pToken){
        ApiResponse lAPIResponse = new ApiResponse();
        System.out.println(pToken);
        return  lAPIResponse = gPasswordService.validateFrgPassurl(pToken);
    }

}
