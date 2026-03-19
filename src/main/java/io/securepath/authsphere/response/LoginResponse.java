package io.securepath.authsphere.response;

import io.securepath.authsphere.models.Users;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginResponse {
    private long userID;
    private String userName;
    private String Role;
    private String otpExpiration_Time;

    //Need to send permission list here and role as well

}
