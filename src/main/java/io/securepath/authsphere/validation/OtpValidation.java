package io.securepath.authsphere.validation;

import io.securepath.authsphere.IOJwt.JwtUtility;
import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.request.UserRequest;
import io.securepath.authsphere.response.ApiResponse;
import org.aspectj.weaver.ast.Var;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class OtpValidation {

    public ApiResponse otpValidate(Users pUser, UserRequest pUserReq) throws Exception {
        ApiResponse lApiResponse = new ApiResponse();
        String lOtpExpTime = "";

        //OTP from Request
        var lOtp = pUserReq.getOtp();
        System.out.println(lOtp);
        if (lOtp == null || lOtp.isEmpty()) {
            lApiResponse.setStatusCode("001");
            return lApiResponse;
        }

        //Compare the OTP
        if (!lOtp.equalsIgnoreCase(pUser.getOTP())) {
            lApiResponse.setStatusCode("002");
            return lApiResponse;
        }
        lApiResponse.setStatusCode("00");
        lApiResponse.setResponse("Otp Validate Success");
        lApiResponse.setToken(JwtUtility.refreshToken(pUser));
        //Check Otp Expiration Time Here
        return lApiResponse;
    }
}
