package io.securepath.authsphere.Utilitys;

import io.securepath.authsphere.constants.RedisFunctions;
import io.securepath.authsphere.cryptography.AESEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.securepath.authsphere.cryptography.AESEncryption.hashPasswordWithKey;

@Component
public class PasswordPolices {
    private final RedisFunctions redisFunctions;

    public PasswordPolices(RedisFunctions redisFunctions) {
        this.redisFunctions = redisFunctions;
    }

    public  String generateRestPassUrl(String pUserId, String pUsername) {
        String lRestURL = "http://localhost:9000/passwordpolicy/rest/password?key=";
        String lRequestToke = pUsername + "@" + pUserId;
        String callbackUrlTokrn = AESEncryption.encrypt(lRequestToke);
        redisFunctions.storeResetPassToken(String.valueOf(pUserId),callbackUrlTokrn);
        return lRestURL.concat(AESEncryption.encrypt(lRequestToke));
    }



    public Map<Object, Object>  getRestPassDataByUserID(String pUserId) {
        return redisFunctions.findRestTokenByUserId(pUserId);
    }

    public static String createNewPassword(String pNewPass, String pUserKey) {
        return AESEncryption.hashPasswordWithKey(pNewPass, pUserKey);
    }
}
