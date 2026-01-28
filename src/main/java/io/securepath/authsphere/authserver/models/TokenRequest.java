package io.securepath.authsphere.authserver.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenRequest {
    private String code;
    private String client_id;
    private String client_secret;

}
