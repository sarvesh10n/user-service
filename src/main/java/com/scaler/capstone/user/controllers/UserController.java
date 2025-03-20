package com.scaler.capstone.user.controllers;

import com.scaler.capstone.user.dtos.LoginRequestDTO;
import com.scaler.capstone.user.dtos.LoginResponseDTO;
import com.scaler.capstone.user.dtos.SignUpRequestDTO;
import com.scaler.capstone.user.dtos.UserDTO;
import com.scaler.capstone.user.exception.InvalidDataException;
import com.scaler.capstone.user.exception.InvalidPasswordException;
import com.scaler.capstone.user.exception.UserAlreadyExistException;
import com.scaler.capstone.user.models.Token;
import com.scaler.capstone.user.models.User;
import com.scaler.capstone.user.services.TokenService;
import com.scaler.capstone.user.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private TokenService tokenService;
    
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignUpRequestDTO requestDTO) throws UserAlreadyExistException, InvalidPasswordException, InvalidDataException {
        User user = userService.createUser(requestDTO.getEmail(),
                requestDTO.getPassword(), requestDTO.getName(), requestDTO.getStreet(), requestDTO.getCity(),
                requestDTO.getState(), requestDTO.getZipcode(), requestDTO.getCountry(), requestDTO.getRoles());

        return new ResponseEntity<>(UserDTO.fromUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO) {
        Token token = tokenService.login(requestDTO.getEmail(), requestDTO.getPassword());
        return new ResponseEntity<>(LoginResponseDTO.fromToken(token), HttpStatus.OK);
    }

    @PostMapping("/validate/{token}")
    public ResponseEntity<UserDTO> validateUser(@PathVariable String token) {
        User user = tokenService.validateToken(token);
        return new ResponseEntity<>(UserDTO.fromUser(user), HttpStatus.OK);
    }
}