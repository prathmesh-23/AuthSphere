package io.securepath.authsphere.validation;

import io.securepath.authsphere.IOJwt.JwtUtility;
import io.securepath.authsphere.constants.ErrorConstant;
import io.securepath.authsphere.constants.RedisConstant;
import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.response.ApiResponse;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class OtpValidation {

    public ApiResponse otpValidate(Users pUser, String pReqOtp) throws Exception {
        var lOtpExpInMinutes = RedisConstant.OTP_EXP_TIME;
        LocalDateTime lCurrentTime = LocalDateTime.now();
        LocalDateTime lOTPGenTime = pUser.getOtpexptime();

        ApiResponse lApiResponse = new ApiResponse();
        if (pUser.getOTP() == null || pReqOtp == null) {
            lApiResponse.setStatus(ErrorConstant.FALIURE);
            lApiResponse.setResponse("OTP NOT VALID");
            return lApiResponse;
        }
        //Compare the OTP
        if (!pReqOtp.equalsIgnoreCase(pUser.getOTP())) {
            lApiResponse.setStatus(ErrorConstant.FALIURE);
            lApiResponse.setResponse("OTP IS INCORRECT");
            return lApiResponse;
        }

        long lOtp_duration_in_min = Duration.between(lCurrentTime, lOTPGenTime).toMinutes();
        //checking time duration between otp genrate and request otp
        if (!(lOtp_duration_in_min <= lOtpExpInMinutes)) {
            lApiResponse.setStatus(ErrorConstant.FALIURE);
            lApiResponse.setResponse("OTP HAS EXPIRED");
            return lApiResponse;
        }
        lApiResponse.setStatus(ErrorConstant.SUCCESS);
        lApiResponse.setResponse("OTP VALIDATE SUCCESS");
        lApiResponse.setToken(JwtUtility.refreshToken(pUser));
        return lApiResponse;
        //Check Otp Expiration Time Here
    }
}
