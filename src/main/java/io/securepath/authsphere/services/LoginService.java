package io.securepath.authsphere.services;

import io.securepath.authsphere.IOJwt.JwtUtility;
import io.securepath.authsphere.bo.LoginBo;
import io.securepath.authsphere.validation.LoginVald;
import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.request.UserRequest;
import io.securepath.authsphere.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    @Autowired
    private LoginBo gLoginBo;

    public ApiResponse loginProcess(UserRequest login) {
        ApiResponse lApiResponse = new ApiResponse();
        Users lUser = gLoginBo.getUser(login);
        // login.setPassword(AESEncryption.hashPasswordWithKey(login.getPassword(),lUser.getHash_key()));
        System.out.println(lUser);
        System.out.println(login);
        boolean lPwsChk = LoginVald.passWordAuthenticate(lUser.getPassEnc(), login.getPassword(), lUser.getHash_key());
        if (lPwsChk) {
            lApiResponse.setResponse("success");

            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", "prathmesh");
            claims.put("role", lUser.getUserName());
            claims.put("userid", lUser.getUserid());
            claims.put("ip", "admin");

            String sessioniD = JwtUtility.createToken(claims);
            lApiResponse.setToken(sessioniD);
            lApiResponse.setStatusCode("200");
            lApiResponse.setResponse("Need to send Permissons");
            return lApiResponse;
        }
        lApiResponse.setResponse("password Wrong");
        return lApiResponse;
    }


}
