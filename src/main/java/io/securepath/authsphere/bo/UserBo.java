package io.securepath.authsphere.bo;

import io.securepath.authsphere.cryptography.AESEncryption;
import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.repository.UserRepo;
import io.securepath.authsphere.request.UserRequest;
import io.securepath.authsphere.response.ApiResponse;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserBo {

    @Autowired
    private UserRepo gUserDao;

    public List<Users> getUsersBo(UserRequest pUsesReq) throws Exception {
        List<Users> lRsp = null;
        System.out.println(pUsesReq.toString());
        lRsp = gUserDao.getUsers();
        System.out.println(lRsp.toString());
        return lRsp;
    }

    public Users getUserBo(UserRequest pUsesReq) throws Exception {
        Users lUser = gUserDao.getUser(pUsesReq.getUserId());
        System.out.println(lUser);
        return lUser;
    }

    public ApiResponse createUserBo(UserRequest pUser) throws Exception {
        ApiResponse lResp = new ApiResponse();
        String lUserPassHashKey = AESEncryption.generateStrongKey();
        long lResult = gUserDao.createUser(pUser.getUserName(), AESEncryption.encrypt(pUser.getPassword()), AESEncryption.hashPasswordWithKey(pUser.getPassword(), lUserPassHashKey), lUserPassHashKey, 1, 1);
        if (lResult == 1) {
            lResp.setResponse("USER CREATED");
        } else {
            lResp.setResponse("USER NOT CREATED");
        }
        lResp.setStatusCode("200");
        lResp.setStatus("OK");

        return lResp;
    }

    public ApiResponse updateUserBo(UserRequest pUser) throws Exception {
        ApiResponse lResp = new ApiResponse();
        long lResult = gUserDao.updateUserDao(pUser.getUserName(), pUser.getEmail(), pUser.getUserId());
        if (lResult == 1) {
            lResp.setResponse("USER UPDATE");
            lResp.setStatusCode("200");
            lResp.setStatus("OK");
        } else {
            lResp.setResponse("USER NOT UPDATE ");
            lResp.setStatusCode("400");
            lResp.setStatus("OK");
        }

        return lResp;
    }
}
