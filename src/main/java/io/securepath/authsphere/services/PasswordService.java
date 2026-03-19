package io.securepath.authsphere.services;

import io.securepath.authsphere.Utilitys.PasswordPolices;
import io.securepath.authsphere.bo.UserBo;
import io.securepath.authsphere.constants.ErrorConstant;
import io.securepath.authsphere.constants.RedisFunctions;
import io.securepath.authsphere.controller.PasswordControl;
import io.securepath.authsphere.cryptography.AESEncryption;
import io.securepath.authsphere.models.Users;
import io.securepath.authsphere.repository.UserRepo;
import io.securepath.authsphere.request.ChangePassRequest;
import io.securepath.authsphere.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class PasswordService {

    private static final Logger glogger = LoggerFactory.getLogger(PasswordService.class);

    private final RedisFunctions redisFunctions;

    @Autowired
    private UserRepo gUserDao;

    @Autowired
    private UserBo gUserBo;

    @Autowired
    private PasswordPolices gPasswordPolices;

    public PasswordService(RedisFunctions redisFunctions) {
        this.redisFunctions = redisFunctions;
    }

    public ApiResponse validateFrgPassurl(String pReqToken) {
        ApiResponse lAPIResponse = new ApiResponse();
        try {
            if (pReqToken == null) {
                lAPIResponse.setStatus(ErrorConstant.ERROR);
                return lAPIResponse;
            }

            String lDecryptToken = AESEncryption.decrypt(pReqToken);
            String lUserId= lDecryptToken.split("@")[1];
            Map<Object,Object> lTokenData= redisFunctions.findRestTokenByUserId(lUserId);
            if (lTokenData==null) {
                lAPIResponse.setStatus(ErrorConstant.FALIURE);
                lAPIResponse.setResponse("LINK HAS EXPIRED");
                return lAPIResponse;
            }
            String lToken = lTokenData.get("reset_pass_token").toString();
            if (!pReqToken.equals(lToken) ) {
                lAPIResponse.setStatus(ErrorConstant.ERROR);
                lAPIResponse.setResponse("INVALID TOKEN");
            }
            lAPIResponse.setStatus(ErrorConstant.SUCCESS);
        } catch (Exception e) {
            glogger.error("Validate Forgot Password url: ", e);
        }
        return lAPIResponse;
    }

    public ApiResponse restPassword(ChangePassRequest passRequestChangePassRequest) {
        ApiResponse lAPIResponse = new ApiResponse();
        try {
            Users lUser = gUserDao.getUser(passRequestChangePassRequest.getUserId());
            if (lUser == null) {
                lAPIResponse.setStatus(ErrorConstant.FALIURE);
                lAPIResponse.setResponse("USER NOT FOUND");
                return lAPIResponse;
            }
            String lNewHashPass = PasswordPolices.createNewPassword(passRequestChangePassRequest.getNewpassword(), lUser.getHash_key());

            if (!lNewHashPass.equalsIgnoreCase("")) {
                lAPIResponse.setStatus(ErrorConstant.ERROR);
                return lAPIResponse;
            }
            int lResult = gUserBo.setUserNewPassword(lNewHashPass, lUser.getUserid());
            if (lResult == 0) {
                lAPIResponse.setStatus(ErrorConstant.ERROR);
                return lAPIResponse;
            }
            lAPIResponse.setStatus(ErrorConstant.SUCCESS);
        } catch (Exception e) {
            glogger.error("Rest Password Service: ", e);

        }
        return lAPIResponse;
    }
}
