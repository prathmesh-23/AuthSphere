package io.securepath.authsphere.constants;

public class RedisConstant {

    public static int OTP_EXP_TIME = Integer.parseInt(RedisFunctions.SECRETS.get("otp_exp_time"));


}
