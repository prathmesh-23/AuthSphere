package io.securepath.authsphere.bo;

import io.securepath.authsphere.repository.LoginRepo;
import io.securepath.authsphere.cryptography.AESEncryption;
import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.repository.UserRepo;
import io.securepath.authsphere.request.UserRequest;
import io.securepath.authsphere.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginBo {
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

    public void setUserOtp(String pOtp, long pUserId) {
        try {
            System.out.println(pOtp + " " + pUserId);
           int res = gLoginDao.updateOtp(pOtp,pUserId);
           System.out.println(res);

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }
}
