package io.securepath.authsphere.services;

import io.securepath.authsphere.IOJwt.JwtUtility;
import io.securepath.authsphere.Utilitys.OTP;
import io.securepath.authsphere.Utilitys.PasswordPolices;
import io.securepath.authsphere.bo.LoginBo;
import io.securepath.authsphere.bo.UserBo;
import io.securepath.authsphere.constants.EmailSubject;
import io.securepath.authsphere.constants.ErrorConstant;
import io.securepath.authsphere.constants.RedisFunctions;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

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

    @Autowired
    private PasswordPolices gPasswordPolices;

    public ApiResponse loginProcess(UserRequest login) {
        ApiResponse lApiResponse = new ApiResponse();
        Users lUser = gLoginBo.getUser(login);
        LocalDateTime lOtpExpirationTime = LocalDateTime.now().minusMinutes(10);
        boolean lPwsChk = LoginVald.passWordAuthenticate(lUser.getPassEnc(), login.getPassword(), lUser.getHash_key());
        if (lPwsChk) {
            lApiResponse.setResponse(ErrorConstant.SUCCESS);
            String lOtp = OTP.generateOtp();
            HashMap<String, String> lEmailValue = new HashMap<>();
            lEmailValue.put("username", lUser.getUserName());
            lEmailValue.put("otp", lOtp);
            lEmailValue.put("expiryMinutes", "10");
            Runnable lRunnable = () -> {
                try {
                    gEmailSend.sendOtpEmail(lEmailValue, EmailSubject.OTP_VERIFICATION, AESEncryption.decrypt(lUser.getEmailEnc()));
                } catch (Exception e) {
                    glogger.error("Email sending failed", e);
                }
            };
            new Thread(lRunnable).start();

            gLoginBo.setUserOtp(lOtp, lOtpExpirationTime, lUser.getUserid());
            LoginResponse lResponse = new LoginResponse();

            lResponse.setRoleId(lUser.getRoleid());
            lResponse.setUserId(lUser.getUserid());
            lResponse.setUserName(lUser.getUserName());

            lApiResponse.setToken(JwtUtility.setClaims(lUser));
            lApiResponse.setStatusCode(ErrorConstant.SUCCESS);
            lApiResponse.setResponse(lResponse);
            return lApiResponse;
        }
        lApiResponse.setResponse("WRONG PASSWORD");
        lApiResponse.setStatus(ErrorConstant.FALIURE);
        return lApiResponse;
    }


    public ApiResponse otpValidate(UserRequest pOtpRequest) {
        ApiResponse lApiResponse = new ApiResponse();
        try {
            Users lUser = gLoginBo.getUser(pOtpRequest);
            if (lUser == null || lUser.getEmailEnc().isEmpty()) {
                lApiResponse.setStatus(ErrorConstant.FALIURE);
                lApiResponse.setResponse("USER NOT FOUND");
                return lApiResponse;
            }
            lApiResponse = gOtpValidation.otpValidate(lUser, pOtpRequest.getOtp());
        } catch (Exception e) {
            glogger.error("Exception in forgotPassService " + e);
            lApiResponse.setStatus(ErrorConstant.ERROR);
        }

        return lApiResponse;
    }

    public ApiResponse forgotPassService(UserRequest pUserRequest) {
        ApiResponse lApiResponse = new ApiResponse();
        LocalDateTime lOtpExpirationTime = LocalDateTime.now().minusMinutes(10);
        try {
            Users lUser = gLoginBo.getUser(pUserRequest);
            if (lUser == null || lUser.getEmailEnc().isEmpty()) {
                lApiResponse.setStatus(ErrorConstant.FALIURE);
                lApiResponse.setResponse("USER NOT FOUND");
                return lApiResponse;
            }

            HashMap<String, String> lEmailValue = new HashMap<>();
            lEmailValue.put("username", lUser.getUserName());
            lEmailValue.put("resetLink", gPasswordPolices.generateRestPassUrl(String.valueOf(lUser.getUserid()),lUser.getUserName()));
            lEmailValue.put("expiryHours", "1");
            //check the user active or not

            Runnable lRunnable = () -> {
                try {
                    gEmailSend.sendEmail(lEmailValue, EmailSubject.FORGGOT_PASSWORD, AESEncryption.decrypt(lUser.getEmailEnc()));
                } catch (Exception e) {
                    glogger.error("Email sending failed", e);
                }
            };
            new Thread(lRunnable).start();


            lApiResponse.setStatus(ErrorConstant.SUCCESS);
            lApiResponse.setResponse("URL HAS SEND TO EMAIL");
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
