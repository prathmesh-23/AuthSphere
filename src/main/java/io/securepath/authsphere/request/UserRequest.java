package io.securepath.authsphere.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequest {
    private long userId;
    private String email;
    private String userName;
}
