package com.fundamentals.spa.repository;

import com.fundamentals.spa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("""
            SELECT u.email FROM User u WHERE u.id = :id
            """)
    Optional<String> getEmailById(@Param("id") UUID id);
}
