package io.securepath.authsphere.services;

import io.securepath.authsphere.IOJwt.JwtUtility;
import io.securepath.authsphere.Utilitys.OTP;
import io.securepath.authsphere.bo.LoginBo;
import io.securepath.authsphere.bo.UserBo;
import io.securepath.authsphere.response.LoginResponse;
import io.securepath.authsphere.validation.LoginVald;
import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.request.UserRequest;
import io.securepath.authsphere.response.ApiResponse;
import io.securepath.authsphere.validation.OtpValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    private LoginBo gLoginBo;
    @Autowired
    private UserBo gUserBo;

    @Autowired
    private OtpValidation gOtpValidation;


    public ApiResponse loginProcess(UserRequest login) {
        ApiResponse lApiResponse = new ApiResponse();
        Users lUser = gLoginBo.getUser(login);
        // login.setPassword(AESEncryption.hashPasswordWithKey(login.getPassword(),lUser.getHash_key()));
        System.out.println(lUser);
        System.out.println(login);
        boolean lPwsChk = LoginVald.passWordAuthenticate(lUser.getPassEnc(), login.getPassword(), lUser.getHash_key());
        if (lPwsChk) {
            lApiResponse.setResponse("success");
            String lOtp = OTP.generateOtp();
            gLoginBo.setUserOtp(lOtp,lUser.getUserid());
            Users User = new Users();
//            User.setOtp(lOtp);
            User.setUserName(lUser.getUserName());
            User.setIsactive(lUser.getIsactive());
            User.setIsdeleted(lUser.getIsdeleted());

            LoginResponse  lResponse = new LoginResponse();

            lResponse.setOtp(lOtp);
            lResponse.setUser(User);
            lResponse.setOtpExpiration_Time("200Minute");

//
//            Map<String, Object> claims = new HashMap<>();
//            claims.put("sub", "prathmesh");
//            claims.put("role", lUser.getUserName());
//            claims.put("userid", lUser.getUserid());
//            claims.put("ip", "admin");

//            String sessioniD =
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
        return gOtpValidation.otpValidate(lUser,pOtpRequest);
    }
}
