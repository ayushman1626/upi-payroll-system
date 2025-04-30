package com.upipaysystem.payroll.service;

import com.upipaysystem.payroll.dtos.common.UserDTO;
import com.upipaysystem.payroll.model.User;
import com.upipaysystem.payroll.repo.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }



    public UserDTO getUser(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty())
            throw new UsernameNotFoundException("User not found");
        return new UserDTO(user.get());
    }
}
