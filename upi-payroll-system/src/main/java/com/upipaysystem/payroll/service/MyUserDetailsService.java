package com.upipaysystem.payroll.service;

import com.upipaysystem.payroll.model.User;
import com.upipaysystem.payroll.model.UserPrinciple;
import com.upipaysystem.payroll.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = repo.findByEmail(email);
        if(!user.isPresent()){
            throw new UsernameNotFoundException("Username not found . 404");
        }
        return new UserPrinciple(user.get());
    }

}
