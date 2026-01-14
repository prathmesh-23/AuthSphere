package io.securepath.authsphere.repository;

import io.securepath.authsphere.constants.Queryconstant;
import io.securepath.authsphere.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<Users,Long> {

    @Query(value = "SELECT * FROM Users WHERE userid=:pUserId", nativeQuery = true)
    //public List<Users> getUsers(long pUserId);
    public List<Users> getUsers(@Param("pUserId") long pUserId);
}
