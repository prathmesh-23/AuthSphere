package io.securepath.authsphere.repository;

import io.securepath.authsphere.models.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.security.URIParameter;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    public static String GET_USERS = "SELECT * FROM Users";
    public static String GET_USER_BY_ID = "SELECT * FROM Users WHERE userid=:pUserId";
    public static String CREATE_USER = "INSERT INTO Users(username, email_enc, pass_enc,hash_key, isactive, isdeleted) VALUES (:name, :emailEnc, :passEnc, :Hash_Key, :isActive, :isDeleted)";
    public static String UPDATE_USER = "UPDATE Users SET username=:name, email_enc=:emailEnc WHERE userid=:pUserId";

    @Query(value = GET_USERS, nativeQuery = true)
    public List<Users> getUsers() throws Exception;
    //public List<Users> getUsers();

    @Query(value = GET_USER_BY_ID, nativeQuery = true)
    public Users getUser(@Param("pUserId") long pUserId) throws Exception;


    @Modifying
    @Transactional
    @Query(value = CREATE_USER, nativeQuery = true)
    int createUser(@Param("name") String pName,
                    @Param("emailEnc") String pEmailEnc,
                    @Param("passEnc") String pPassEnc,
                    @Param("Hash_Key") String pHashKey,
                    @Param("isActive") int pIsActive,
                    @Param("isDeleted") int pIsDeleted) throws Exception;

    @Modifying
    @Query(value = UPDATE_USER, nativeQuery = true)
    int updateUserDao(@Param("name") String pName,
                       @Param("emailEnc") String pEmailEnc,
                       @Param("pUserId") long pUserId) throws Exception;
}
