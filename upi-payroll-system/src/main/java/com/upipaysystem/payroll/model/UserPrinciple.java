package com.upipaysystem.payroll.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserPrinciple implements UserDetails {

    private User user;
    public UserPrinciple(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public Long getOrganizationId(){
        return user.getOrganization().getId();
    }

    public Optional<Organization> getOrganization(){
        Optional<Organization> optionalOrganization =
                user.getOrganization() != null ? Optional.ofNullable(user.getOrganization()) : Optional.empty();
        return optionalOrganization;
    }
}
