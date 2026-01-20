package io.securepath.authsphere.services;

import io.securepath.authsphere.bo.UserBo;
import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.request.UserRequest;
import io.securepath.authsphere.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserBo gUserBo;

    public ApiResponse getUsersList(UserRequest pUserReq) throws Exception {
        ApiResponse lResp = new ApiResponse();
            List<Users> lUserList = null;
            lUserList = gUserBo.getUsersBo(pUserReq);
            if (!(lUserList == null)) {
                lResp.setResponse(lUserList);
                lResp.setStatus("Ok");
                lResp.setStatusCode("200");
            }

        return lResp;
    }

    public ApiResponse getUserDetails(UserRequest pUserReq) throws Exception {
        ApiResponse lResp = new ApiResponse();
            Users lUser = gUserBo.getUserBo(pUserReq);
            if (lUser!=null){
                lResp.setStatus("OK");
                lResp.setStatusCode("200");
                lResp.setResponse(lUser);
            }else {
                lResp.setStatus("USER NOT FOUND");
                lResp.setStatusCode("200");
                lResp.setResponse(null);
            }

        return lResp;
    }

    public ApiResponse createUser(UserRequest pUser) throws Exception {
        return gUserBo.createUserBo(pUser);
    }

    public ApiResponse updateUser(UserRequest pUser) throws Exception {
        return gUserBo.updateUserBo(pUser);
    }
}


