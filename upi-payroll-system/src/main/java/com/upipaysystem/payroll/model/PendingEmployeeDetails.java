package com.upipaysystem.payroll.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pending_employee_details")
public class PendingEmployeeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pending_user_id")
    private PendingUser pendingUser;

    private EmployeeDetails.Gender gender;

    private String designation;

    private Double salary;

    private String department;

    private LocalDate joiningDate;

    private String upiId;

    private LocalDateTime createdAt;



    public EmployeeDetails.Gender getGender() {
        return gender;
    }

    public void setGender(EmployeeDetails.Gender gender) {
        this.gender = gender;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PendingUser getPendingUser() {
        return pendingUser;
    }

    public void setPendingUser(PendingUser pendingUser) {
        this.pendingUser = pendingUser;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public String toString() {
        return "PendingEmployeeDetails{" +
                "id=" + id +
                ", pendingUser=" + pendingUser +
                ", designation='" + designation + '\'' +
                ", salary=" + salary +
                ", department='" + department + '\'' +
                ", joiningDate=" + joiningDate +
                '}';
    }
}
