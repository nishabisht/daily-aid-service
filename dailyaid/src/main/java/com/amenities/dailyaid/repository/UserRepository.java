package com.amenities.dailyaid.repository;

import com.amenities.dailyaid.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserData,Long> {

    //custom query method to find a user by username
    Optional<UserData> findByUsername(String username);

    //find user by email id
    Optional<UserData> findByEmail(String email);

    //delete user by name

    //update user data

}
