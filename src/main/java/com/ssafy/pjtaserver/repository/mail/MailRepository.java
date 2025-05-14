package com.ssafy.pjtaserver.repository.mail;

import com.ssafy.pjtaserver.domain.mail.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findByEmail(String email);
}
