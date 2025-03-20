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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignUpRequestDTO requestDTO) throws UserAlreadyExistException, InvalidPasswordException, InvalidDataException {
        User user = userService.createUser(requestDTO.getEmail(),
                requestDTO.getPassword(), requestDTO.getName(), requestDTO.getStreet(), requestDTO.getCity(),
                requestDTO.getState(), requestDTO.getZipcode(), requestDTO.getCountry(), requestDTO.getRoles());

        return new ResponseEntity<>(UserDTO.fromUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/getuser/all")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')") //This will enable role based access
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> userList = userService.getAllUser();
        List<UserDTO> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(UserDTO.fromUser(user));
        }
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @GetMapping("/getuser/{email}")
    public ResponseEntity<UserDTO> getUsersByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return new ResponseEntity<>(UserDTO.fromUser(user), HttpStatus.OK);
    }

    @GetMapping("/getuser/{id}")
    public ResponseEntity<UserDTO> getAllUsers(@PathVariable Long id) {
        User user = userService.getUserByEmail(id);
        return new ResponseEntity<>(UserDTO.fromUser(user), HttpStatus.OK);
    }
}