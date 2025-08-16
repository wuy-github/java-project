package com.project.futabuslines.repositories;

import com.project.futabuslines.models.Token;
import com.project.futabuslines.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUser(User user);
    Optional<Token> findByToken(String token);
}