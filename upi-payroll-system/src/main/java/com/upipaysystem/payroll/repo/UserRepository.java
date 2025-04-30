package com.upipaysystem.payroll.repo;

import com.upipaysystem.payroll.model.Organization;
import com.upipaysystem.payroll.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);

    List<User> findAllByOrganizationAndRole(Organization organization, User.Role role);
}
