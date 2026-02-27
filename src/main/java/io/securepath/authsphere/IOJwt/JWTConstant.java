package io.securepath.authsphere.IOJwt;

import io.securepath.authsphere.constants.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class JWTConstant {

    public static String SECRET = ConfigProperties.SECRETS.get("SECRET");

}
