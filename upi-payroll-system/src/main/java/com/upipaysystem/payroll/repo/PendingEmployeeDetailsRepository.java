package com.upipaysystem.payroll.repo;

import com.upipaysystem.payroll.model.PendingEmployeeDetails;
import com.upipaysystem.payroll.model.PendingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingEmployeeDetailsRepository extends JpaRepository<PendingEmployeeDetails,Long> {
    //PendingEmployeeDetails findByPendingUser(PendingUser pendingUser);
    PendingEmployeeDetails findByPendingUser_VerificationToken(String token);
}
