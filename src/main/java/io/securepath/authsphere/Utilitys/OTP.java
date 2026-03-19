package io.securepath.authsphere.Utilitys;

import java.security.SecureRandom;
import java.util.UUID;

public class OTP {

    private static final SecureRandom random = new SecureRandom();

    public static String generateOtp() {
        int number = 100000 + random.nextInt(900000); // ensures 6 digits
        return String.valueOf(number);
    }

//    public static void main(String[] args) {
//        System.out.println(generateOtp()); // e.g., 482913
//    }

}
