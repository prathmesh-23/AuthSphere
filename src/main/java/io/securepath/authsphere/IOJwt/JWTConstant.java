package io.securepath.authsphere.IOJwt;

import io.securepath.authsphere.constants.RedisFunctions;


public class JWTConstant {

    public static String SECRET = RedisFunctions.SECRETS.get("SECRET");

}
