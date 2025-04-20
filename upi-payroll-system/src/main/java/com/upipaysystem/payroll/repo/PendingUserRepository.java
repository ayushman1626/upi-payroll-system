package com.upipaysystem.payroll.repo;

import com.upipaysystem.payroll.model.PendingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingUserRepository extends JpaRepository<PendingUser, Long>{
    boolean existsByEmail(String email);

    Optional<PendingUser> findByEmail(String email);
}
