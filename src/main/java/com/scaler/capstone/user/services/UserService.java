package com.scaler.capstone.user.services;

import com.scaler.capstone.user.exception.InvalidDataException;
import com.scaler.capstone.user.exception.InvalidPasswordException;
import com.scaler.capstone.user.exception.UserAlreadyExistException;
import com.scaler.capstone.user.models.Address;
import com.scaler.capstone.user.models.Role;
import com.scaler.capstone.user.models.User;
import com.scaler.capstone.user.repositories.RoleRepository;
import com.scaler.capstone.user.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final String PASSWORD_REGEX =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    private final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    private BCryptPasswordEncoder bcryptpasswordencoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bcryptpasswordencoder,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bcryptpasswordencoder = bcryptpasswordencoder;
        this.roleRepository = roleRepository;
    }

    public User createUser(String email, String password, String name, String street,
                           String city, String state, String zip, String country, List<String> roles) throws UserAlreadyExistException, InvalidPasswordException,InvalidDataException {


        if(!isValidPassword(password))
        {
            throw new InvalidPasswordException("Invalid Password. Password should be at least 8 characters long " +
                    "and should have at least one digit, one uppercase letter, " +
                    "one lowercase letter and one special character");
        }

        List<Role> roleList = new ArrayList<>();
        if(!roles.isEmpty())
        {
            for(String role : roles)
            {
                Optional<Role> roleOptional = roleRepository.findByName(role);
                if(roleOptional.isPresent())
                {
                    roleList.add(roleOptional.get());
                }
                else
                {
                    Role newRole = new Role();
                    newRole.setName(role);
                    roleList.add(roleRepository.save(newRole));
                }
            }
        }
        else
        {
            throw new InvalidDataException("Roles is mandatory while creating user");
        }

        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isEmpty()) {
            throw new UserAlreadyExistException("User already present: " + email);
        }

        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setZipcode(zip);
        address.setCountry(country);

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setHashedPassword(bcryptpasswordencoder.encode(password));
        newUser.setAddress(address);
        newUser.setRoles(roleList);
        return userRepository.save(newUser);
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
