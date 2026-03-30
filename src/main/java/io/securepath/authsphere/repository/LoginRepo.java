package io.securepath.authsphere.repository;

import io.securepath.authsphere.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface LoginRepo extends JpaRepository<Users, Long> {


    public static String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email_enc=:emailEnc";
    public static String UPDATE_OTP = "UPDATE users SET otp = :otp, otpexptime = :otpExpTime WHERE userid = :userId";
   public static String UPDATE_PASSWORD_ATTEMPTS = "UPDATE users set passattempt= passattempt+1 WHERE userid = :pUserId";
public static String UPDATE_USER = "UPDATE Users SET username=:name, email_enc=:emailEnc WHERE userid=:pUserId";
    public static String RESET_PASSWORD_ATTEMPTS = "UPDATE users set passattempt=0 WHERE userid = :pUserId";

    @Query(value = GET_USER_BY_EMAIL, nativeQuery = true)
    public Users getUserByEmail(@Param("emailEnc") String pUserEmail) throws Exception;

//    @Modifying
//    @Query(value = UPDATE_OTP, nativeQuery = true)
//    public void updateOtp(@Param("genrated_otp") String pOtp, @Param("UserId") long pUserId) throws Exception;

    @Transactional
    @Modifying
    @Query(value = UPDATE_OTP, nativeQuery = true)
    int updateOtp(@Param("otp") String otp, @Param("otpExpTime") LocalDateTime otpExpTime, @Param("userId") long userId);

    @Transactional
    @Modifying
    @Query(value = UPDATE_PASSWORD_ATTEMPTS, nativeQuery = true)
    int updatePasswordAttempt(@Param("pUserId") long pUserId);

    @Transactional
    @Modifying
    @Query(value = RESET_PASSWORD_ATTEMPTS, nativeQuery = true)
    void restPasswordAttempt(long pUserId);
}
