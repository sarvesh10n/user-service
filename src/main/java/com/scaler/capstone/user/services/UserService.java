package com.scaler.capstone.user.services;

import com.scaler.capstone.user.dtos.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserDTO createUser(String email, String password, String name)
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setName(name);
        return userDTO;
    }
}
