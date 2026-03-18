package io.securepath.authsphere.controller;


import io.securepath.authsphere.request.ChangePassRequest;
import io.securepath.authsphere.response.ApiResponse;
import io.securepath.authsphere.services.PasswordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passwordpolicy")
public class PasswordControl {
    private static final Logger glogger = LoggerFactory.getLogger(PasswordControl.class);

    @Autowired
    private PasswordService gPasswordService;

    @GetMapping("rest/password")
    public ApiResponse restpassword(@RequestParam("key") String pToken) {
        ApiResponse lAPIResponse = new ApiResponse();
        try {
            lAPIResponse = gPasswordService.validateFrgPassurl(pToken);
        } catch (Exception e) {
            glogger.error("Error in restpassword token validation", e);
        }
        return lAPIResponse;
    }

    @PostMapping("rest/password")
    public ApiResponse resetpassword(@RequestBody ChangePassRequest passRequestChangePassRequest) {
        ApiResponse lAPIResponse = new ApiResponse();
        try {
            lAPIResponse = gPasswordService.restPassword(passRequestChangePassRequest);
        } catch (Exception e) {
            glogger.error("Error in reset the new password", e);
        }
        return lAPIResponse;
    }

}
