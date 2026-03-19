package io.securepath.authsphere.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangePassRequest {
    private long userId;
    private String newpassword="";
}
