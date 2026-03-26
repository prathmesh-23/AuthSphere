package io.securepath.authsphere.models;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRole {

    @Column(name = "userid")
    private Long userId;

    @Column(name = "username", nullable = false, length = 100)
    private String userName;

    @Column(name = "role_name", nullable = false, unique = true, length = 100)
    private String rolename="";
}
