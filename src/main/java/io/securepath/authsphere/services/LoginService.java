package io.securepath.authsphere.services;

import io.securepath.authsphere.IOJwt.JwtUtility;
import io.securepath.authsphere.Utilitys.OTP;
import io.securepath.authsphere.bo.LoginBo;
import io.securepath.authsphere.bo.UserBo;
import io.securepath.authsphere.constants.EmailSubject;
import io.securepath.authsphere.constants.ErrorConstant;
import io.securepath.authsphere.controller.Login;
import io.securepath.authsphere.cryptography.AESEncryption;
import io.securepath.authsphere.notifications.EmailSend;
import io.securepath.authsphere.response.LoginResponse;
import io.securepath.authsphere.validation.LoginVald;
import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.request.UserRequest;
import io.securepath.authsphere.response.ApiResponse;
import io.securepath.authsphere.validation.OtpValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {

    private static final Logger glogger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private LoginBo gLoginBo;
    @Autowired
    private UserBo gUserBo;

    @Autowired
    private EmailSend gEmailSend;

    @Autowired
    private OtpValidation gOtpValidation;


    public ApiResponse loginProcess(UserRequest login) {
        ApiResponse lApiResponse = new ApiResponse();
        Users lUser = gLoginBo.getUser(login);
        LocalDateTime lOtpExpirationTime = LocalDateTime.now().minusMinutes(10);

        // login.setPassword(AESEncryption.hashPasswordWithKey(login.getPassword(),lUser.getHash_key()));
        System.out.println(lUser);
        System.out.println(login);
        boolean lPwsChk = LoginVald.passWordAuthenticate(lUser.getPassEnc(), login.getPassword(), lUser.getHash_key());
        if (lPwsChk) {
            lApiResponse.setResponse("success");
            String lOtp = OTP.generateOtp();
            gLoginBo.setUserOtp(lOtp, lOtpExpirationTime,lUser.getUserid());
//            Users User = new Users();
////            User.setOtp(lOtp);
//            User.setUserName(lUser.getUserName());
//            User.setIsactive(lUser.getIsactive());
//            User.setIsdeleted(lUser.getIsdeleted());

            LoginResponse lResponse = new LoginResponse();

            lResponse.setRole("ADMIN");
            lResponse.setUserID(lUser.getUserid());
            lResponse.setUserName(lUser.getUserName());
            lResponse.setOtpExpiration_Time("200Minute");

            lApiResponse.setToken(JwtUtility.setClaims(lUser));
            lApiResponse.setStatusCode("200");
            lApiResponse.setResponse(lResponse);
            return lApiResponse;
        }
        lApiResponse.setResponse("password Wrong");
        return lApiResponse;
    }


    public ApiResponse otpValidate(UserRequest pOtpRequest) throws Exception {
        Users lUser = gUserBo.getUserBo(pOtpRequest);
        return gOtpValidation.otpValidate(lUser, pOtpRequest);
    }

    public ApiResponse forgotPassService(UserRequest pUserRequest) {
        ApiResponse lApiResponse = new ApiResponse();

        //OTP ExpireTime
        //OTP genrate Time
        LocalDateTime lOtpExpirationTime = LocalDateTime.now().minusMinutes(10);
        try {
            Users lUser = gLoginBo.getUser(pUserRequest);
            if (lUser == null) {
                lApiResponse.setStatus(ErrorConstant.FALIURE);
                lApiResponse.setResponse("USER NOT FOUND");
                return lApiResponse;
            }
            if (lUser.getEmailEnc().isEmpty()) {
                lApiResponse.setStatus(ErrorConstant.FALIURE);
                lApiResponse.setResponse("USER NOT FOUND");
                return lApiResponse;
            }

            //OTP Genrate and store into User detail with otpExprire time
            String lOtp = OTP.generateOtp();
            int otpInsert = gLoginBo.setUserOtp(lOtp,lOtpExpirationTime, lUser.getUserid());
            if (otpInsert != 1) {
                lApiResponse.setStatus(ErrorConstant.ERROR);
                lApiResponse.setResponse("");
                return lApiResponse;
            }

            //check the user active or not
            gEmailSend.sendEmail(AESEncryption.decrypt(lUser.getEmailEnc()), EmailSubject.FORGGOT_PASSWORD, "OPT :123456");

            lApiResponse.setStatus(ErrorConstant.SUCCESS);
            lApiResponse.setResponse("OTP HAS BEEN SENT");
            lApiResponse.setToken(JwtUtility.setClaims(lUser));
        } catch (Exception e) {
            glogger.error("Exception in forgotPassService " + e);
            lApiResponse.setStatus(ErrorConstant.ERROR);
        }
        return lApiResponse;
    }

    public static void main(String[] args) {
        LocalDateTime lOtpExpirationTime = LocalDateTime.now().plusMinutes(10);
        System.out.println(lOtpExpirationTime
        );
    }
}
