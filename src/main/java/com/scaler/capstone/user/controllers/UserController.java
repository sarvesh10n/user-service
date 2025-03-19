package com.scaler.capstone.user.controllers;

import com.scaler.capstone.user.dtos.SignUpRequestDTO;
import com.scaler.capstone.user.dtos.UserDTO;
import com.scaler.capstone.user.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignUpRequestDTO requestDTO){
        UserDTO user = userService.createUser(requestDTO.getEmail(),
                requestDTO.getPassword(), requestDTO.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}