package com.upipaysystem.payroll.controller;

import com.upipaysystem.payroll.dtos.common.ApiResponse;
import com.upipaysystem.payroll.dtos.common.UserDTO;
import com.upipaysystem.payroll.model.UserPrinciple;
import com.upipaysystem.payroll.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(
            @AuthenticationPrincipal UserPrinciple userPrinciple) throws Exception
    {
        if(userPrinciple == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "User not Authenticated",null));
        }
        String email = userPrinciple.getUsername();
        UserDTO user = userService.getUser(email);

//        Map<String, Object> data = new HashMap<>();
//        data.put("data",user);
        return ResponseEntity.ok(new ApiResponse<>(true, "User fetched successfully.",user));
    }
}
