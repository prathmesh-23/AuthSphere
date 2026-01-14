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

    public ApiResponse getUserDetails(UserRequest pUserReq) {
        ApiResponse lResp = new ApiResponse();
        try {
            List<Users> lUserList = null;
            lUserList = gUserBo.getUsersBo(pUserReq);
            if (!(lUserList==null)){
                lResp.setResponse(lUserList);
                lResp.setStatus("Ok");
                lResp.setStatusCode("200");
            }
        } catch (Exception e) {

        }
        return lResp;
    }
}


