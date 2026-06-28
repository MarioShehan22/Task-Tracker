package com.tasktracker.repository;

import com.tasktracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("""
        SELECT DISTINCT u
        FROM User u
        JOIN FETCH u.role r
        LEFT JOIN FETCH r.rolePermissions rp
        LEFT JOIN FETCH rp.permission
        WHERE u.email = :email
    """)
    Optional<User> findByEmailWithPermissions(@Param("email") String email);

    Long countByActiveTrue();

    Long countByActiveFalse();
}