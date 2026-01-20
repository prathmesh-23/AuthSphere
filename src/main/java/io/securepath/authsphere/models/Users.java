package io.securepath.authsphere.models;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Table(name="Users")
@Entity
@Getter
@Setter
@ToString
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL in Postgres
    @Column(name = "userid")
    private Long userid;

    @Column(name = "username", nullable = false, length = 100)
    private String userName;

    @Column(name = "email_enc", nullable = false, unique = true, length = 100)
    private String emailEnc;

    @Column(name = "pass_enc", nullable = false, length = 100)
    private String passEnc;

    @Column(name = "isactive", nullable = false)
    private int isactive;

    @Column(name = "isdeleted", nullable = false)
    private int isdeleted;

    @Column(name = "hash_key", nullable = false)
    private String hash_key;

}
