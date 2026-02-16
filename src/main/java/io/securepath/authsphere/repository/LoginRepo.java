package io.securepath.authsphere.repository;

import io.securepath.authsphere.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepo extends JpaRepository<Users, Long> {


    public static String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email_enc=:emailEnc";

    @Query(value = GET_USER_BY_EMAIL, nativeQuery = true)
    public Users getUserByEmail(@Param("emailEnc") String pUserEmail) throws Exception;

}
