package io.securepath.authsphere.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiResponse {
    private String status="";
    private String statusCode="";
    private Object response;
}
