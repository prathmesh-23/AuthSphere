package io.securepath.authsphere.bo;

import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.repository.UserRepo;
import io.securepath.authsphere.request.UserRequest;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserBo {

    @Autowired
    private UserRepo gUserDao;

    public List<Users> getUsersBo(UserRequest pUsesReq){
        List<Users> lRsp=null;
        System.out.println(pUsesReq.toString());
        lRsp = gUserDao.getUsers(pUsesReq.getUserId());
        System.out.println(lRsp.toString());
        return lRsp;
    }
}
