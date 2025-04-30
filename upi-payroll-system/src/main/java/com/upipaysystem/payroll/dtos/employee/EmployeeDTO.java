package com.upipaysystem.payroll.dtos.employee;

import com.upipaysystem.payroll.model.EmployeeDetails;
import com.upipaysystem.payroll.model.User;
import jakarta.persistence.*;

import java.time.LocalDate;

public class EmployeeDTO {

    private Long id;
    private String name;
    private String designation;
    private String department;

    public EmployeeDTO(User user, EmployeeDetails employee){
        this.id = user.getId();
        this.name = user.getFullName();
        this.designation = employee.getDesignation();
        this.department = employee.getDepartment();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
