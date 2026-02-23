package io.securepath.authsphere.repository;

import io.securepath.authsphere.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepo extends JpaRepository<Users, Long> {


    public static String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email_enc=:emailEnc";
    public static String INSERT_OTP = "UPDATE users SET otp = :otp WHERE user_id = :pUserId";
//    public static String CREATE_USER = "INSERT INTO Users(username, email_enc, pass_enc,hash_key, isactive, isdeleted) VALUES (:name, :emailEnc, :passEnc, :Hash_Key, :isActive, :isDeleted)";

    @Query(value = GET_USER_BY_EMAIL, nativeQuery = true)
    public Users getUserByEmail(@Param("emailEnc") String pUserEmail) throws Exception;

    @Modifying
    @Query(value = INSERT_OTP, nativeQuery = true)
    void updateOtp(@Param("otp") String otp, @Param("pUserId") long userId) throws Exception;

}
