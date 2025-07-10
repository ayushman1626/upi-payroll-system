package com.upipaysystem.payroll.dtos.employee;

import com.upipaysystem.payroll.model.EmployeeDetails;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.aspectj.bridge.IMessage;

import java.time.LocalDate;

public class EmployeeRegisterRequest {

    @NotBlank(message = "name is required")
    private String fullName;

    @Email
    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "Contact no is required")
    private String phone;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "Designation is Required")
    private String designation;

    @NotBlank(message = "Department is required")
    private String department;

    @NotNull(message = "Salary is required")
    private Double salary;

    @NotNull(message = "Gender is required")
    private EmployeeDetails.Gender Gender;

    @NotNull
    private LocalDate joiningDate;

    // Getters and Setters


    public @NotBlank(message = "name is required") String getFullName() {
        return fullName;
    }

    public void setFullName(@NotBlank(message = "name is required") String fullName) {
        this.fullName = fullName;
    }

    public @Email @NotBlank(message = "email is required") String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotBlank(message = "email is required") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Contact no is required") String getPhone() {
        return phone;
    }

    public void setPhone(@NotBlank(message = "Contact no is required") String phone) {
        this.phone = phone;
    }

    public @NotBlank(message = "password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "password is required") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Designation is Required") String getDesignation() {
        return designation;
    }

    public void setDesignation(@NotBlank(message = "Designation is Required") String designation) {
        this.designation = designation;
    }

    public @NotBlank(message = "Department is required") String getDepartment() {
        return department;
    }

    public void setDepartment(@NotBlank(message = "Department is required") String department) {
        this.department = department;
    }

    public @NotNull(message = "Salary is required") Double getSalary() {
        return salary;
    }

    public void setSalary(@NotNull(message = "Salary is required") Double salary) {
        this.salary = salary;
    }

    public @NotNull(message = "Gender is required") EmployeeDetails.Gender getGender() {
        return Gender;
    }

    public void setGender(@NotNull(message = "Gender is required") EmployeeDetails.Gender gender) {
        Gender = gender;
    }

    public @NotNull LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(@NotNull LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

}

