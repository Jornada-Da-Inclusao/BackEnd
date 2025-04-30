package com.fatec.repository;

import com.fatec.model.EmailVerify;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository <EmailVerify, Long>{

    Optional<EmailVerify> findByToken(String token);
}
