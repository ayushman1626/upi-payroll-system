package com.upipaysystem.payroll.dtos.organization;

import com.upipaysystem.payroll.dtos.common.UserDTO;
import com.upipaysystem.payroll.model.Address;
import com.upipaysystem.payroll.model.Organization;
import com.upipaysystem.payroll.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrganizationDetailsDTO {

    private Long id;
    private String name;
    private String OrganizationLocation;
    private String industry;
    private String gstNumber;
    private String registeredEmail;
    private LocalDateTime createdAt;
    private List<UserDTO> users;
    private AddressDTO address;

    public OrganizationDetailsDTO(Organization organization){
        this.id = organization.getId();
        this.name = organization.getName();
        this.gstNumber = organization.getGstNumber();
        this.registeredEmail = organization.getRegisteredEmail();
        this.OrganizationLocation = organization.getOrganizationLocation();
        this.industry = organization.getIndustry() != null ? organization.getIndustry() : null;
        this.address = new AddressDTO(organization.getAddress());
        if (organization.getUsers() != null) {
            this.users = organization.getUsers().stream()
                    .map(UserDTO::new)
                    .collect(Collectors.toList());
        } else {
            this.users = Collections.emptyList(); // Handle the case where there are no users
        }
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

    public String getOrganizationLocation() {
        return OrganizationLocation;
    }

    public void setOrganizationLocation(String organizationLocation) {
        OrganizationLocation = organizationLocation;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getRegisteredEmail() {
        return registeredEmail;
    }

    public void setRegisteredEmail(String registeredEmail) {
        this.registeredEmail = registeredEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}
