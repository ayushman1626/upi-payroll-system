package com.upipaysystem.payroll.repo;

import com.upipaysystem.payroll.model.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails,Long> {

    List<EmployeeDetails> findAllByUserIdIn(List<Long> employeeIds);
}
