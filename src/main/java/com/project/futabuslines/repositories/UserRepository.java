package com.project.futabuslines.repositories;

import com.project.futabuslines.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);
    // SELECT * FROM USERS WHERE phone_number=?
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    long countByCreatedAtAfter(LocalDateTime dateTime);
}
