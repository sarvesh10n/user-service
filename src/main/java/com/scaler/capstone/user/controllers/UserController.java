package com.scaler.capstone.user.controllers;

import com.scaler.capstone.user.dtos.*;
import com.scaler.capstone.user.exception.InvalidDataException;
import com.scaler.capstone.user.exception.InvalidPasswordException;
import com.scaler.capstone.user.exception.UserAlreadyExistException;
import com.scaler.capstone.user.models.Token;
import com.scaler.capstone.user.models.User;
import com.scaler.capstone.user.services.TokenService;
import com.scaler.capstone.user.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignUpRequestDTO requestDTO) throws UserAlreadyExistException, InvalidPasswordException, InvalidDataException {
        User user = userService.createUser(requestDTO);

        return new ResponseEntity<>(UserDTO.fromUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/getallusers")
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            // Extract the JWT token
            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            String username = jwt.getClaim("sub");  // username is email
            if (!email.equalsIgnoreCase(username)) { // Case-insensitive check
                throw new AccessDeniedException("You cannot access another user's data.");
            }
        }
        else {
            throw new BadCredentialsException("Authentication is not valid.");
        }

        User user = userService.getUserByEmail(email);
        return new ResponseEntity<>(UserDTO.fromUser(user), HttpStatus.OK);
    }

    @GetMapping("/getresetpasswordquestion/{email}")
    public ResponseEntity<String> getResetPasswordQuestion(@PathVariable String email) throws InvalidDataException {
        String question = userService.getResetPasswordQuestion(email);
        String jsonResponse = "{\"resetPasswordQuestion\":\""+question+"\"}";
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<UserDTO> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) throws InvalidDataException {
        if(resetPasswordDTO.getEmail()== null || resetPasswordDTO.getResetPasswordQuestion() == null
                || resetPasswordDTO.getResetPasswordAnswer() == null || resetPasswordDTO.getNewPassword() == null){
            throw new InvalidDataException("Invalid Request Body.");
        }
        User user = userService.resetPassword(resetPasswordDTO);
        return new ResponseEntity<>(UserDTO.fromUser(user), HttpStatus.OK);
    }

    @PatchMapping("/addrole/{id}")
    public ResponseEntity<UserDTO> addRole(@PathVariable Long id, @RequestParam String roleName)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            // Extract the JWT token
            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            String userId = jwt.getClaim("userId");  // username is email
            if (!userId.equalsIgnoreCase(String.valueOf(id))) { // Case-insensitive check
                throw new AccessDeniedException("You cannot update another user's data.");
            }
        }
        else {
            throw new BadCredentialsException("Authentication is not valid.");
        }

        User updatedUser = userService.addRole(id,roleName);
        return new ResponseEntity<>(UserDTO.fromUser(updatedUser), HttpStatus.OK);
    }

    @PatchMapping("/removerole/{id}")
    public ResponseEntity<UserDTO> removeRole(@PathVariable Long id, @RequestParam String roleName) throws InvalidDataException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            // Extract the JWT token
            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            String userId = jwt.getClaim("userId");  // username is email
            if (!userId.equalsIgnoreCase(String.valueOf(id))) { // Case-insensitive check
                throw new AccessDeniedException("You cannot update another user's data.");
            }
        }
        else {
            throw new BadCredentialsException("Authentication is not valid.");
        }

        User updatedUser = userService.removeRole(id,roleName);
        return new ResponseEntity<>(UserDTO.fromUser(updatedUser), HttpStatus.OK);
    }

    @PatchMapping("/updateuser/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            // Extract the JWT token
            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            String userId = jwt.getClaim("userId");  // username is email
            if (!userId.equalsIgnoreCase(String.valueOf(id))) { // Case-insensitive check
                throw new AccessDeniedException("You cannot update another user's data.");
            }
        }
        else {
            throw new BadCredentialsException("Authentication is not valid.");
        }

        User updatedUser = userService.updateUser(id,updates);
        return new ResponseEntity<>(UserDTO.fromUser(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/deleteuser/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            // Extract the JWT token
            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            String username = jwt.getClaim("sub");  // username is email
            if (!email.equalsIgnoreCase(username)) { // Case-insensitive check
                throw new AccessDeniedException("You cannot delete another user.");
            }
        }
        else {
            throw new BadCredentialsException("Authentication is not valid.");
        }

        userService.deleteUser(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}