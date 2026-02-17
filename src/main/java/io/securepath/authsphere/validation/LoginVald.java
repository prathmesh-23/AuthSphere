package io.securepath.authsphere.validation;


import io.securepath.authsphere.cryptography.AESEncryption;

public class LoginVald {

    public static boolean passWordAuthenticate(String pUserPassWord, String pUserReqPassword,String pUserHashKey) {
        return AESEncryption.verifyPasswordWithKey(pUserPassWord,pUserReqPassword,pUserHashKey);

    }
}
