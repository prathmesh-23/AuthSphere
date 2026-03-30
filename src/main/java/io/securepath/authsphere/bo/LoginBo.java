package io.securepath.authsphere.bo;

import io.securepath.authsphere.repository.LoginRepo;
import io.securepath.authsphere.cryptography.AESEncryption;
import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.repository.UserRepo;
import io.securepath.authsphere.request.UserRequest;
import io.securepath.authsphere.response.ApiResponse;
import io.securepath.authsphere.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginBo {

    private static final Logger glogger = LoggerFactory.getLogger(LoginBo.class);

    @Autowired
    private LoginRepo gLoginDao;

    public Users getUser(UserRequest login) {
        ApiResponse lApiResponse = new ApiResponse();
        Users lUser = null;
        try {
            lUser = gLoginDao.getUserByEmail(AESEncryption.encrypt(login.getEmail()));
            if (lUser != null) {
                return lUser;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lUser;
    }

    public int setUserOtp(String pOtp, LocalDateTime pOtpExpTime, long pUserId) {
        int res = 0;
        try {
            res = gLoginDao.updateOtp(pOtp, pOtpExpTime, pUserId);
        } catch (Exception e) {
            res = -1;
            glogger.error("Excpetion in Set OTP {}", String.valueOf(e));
        }
        return res;
    }

    public int updatePasswordAttempt(long pUserId) {
        return gLoginDao.updatePasswordAttempt(pUserId);
    }

    public void resteLoginUpdate(long pUserId) {
         gLoginDao.restPasswordAttempt(pUserId);
    }
}
