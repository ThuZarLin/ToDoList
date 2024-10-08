package com.spring.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.main.enitity.Token;
@Repository

public interface TokenRepository extends JpaRepository<Token, Long>{

    @Query("Select t from Token t inner join User u on t.user.id = u.id where t.user.id = :userId and t.isLoggedOut = false")
    List<Token> findAllTokenByUser(Long userId);

    Optional<Token> findByToken(String token);
}
