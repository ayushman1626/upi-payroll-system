package com.upipaysystem.payroll.repo;

import com.upipaysystem.payroll.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepostory extends JpaRepository<Otp,Long> {
    Optional<Otp> findByEmailAndOtp(String trim, String otp);

}
