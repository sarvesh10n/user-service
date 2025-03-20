package com.scaler.capstone.user.services;

import com.scaler.capstone.user.exception.InvalidPasswordException;
import com.scaler.capstone.user.exception.UserAlreadyExistException;
import com.scaler.capstone.user.models.Address;
import com.scaler.capstone.user.models.User;
import com.scaler.capstone.user.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bcryptpasswordencoder) {
        this.userRepository = userRepository;
        this.bcryptpasswordencoder = bcryptpasswordencoder;
    }

    public User createUser(String email, String password, String name, String street,
                           String city, String state, String zip, String country) throws UserAlreadyExistException, InvalidPasswordException {


        if(!isValidPassword(password))
        {
            throw new InvalidPasswordException("Invalid Password. Password should be at least 8 characters long " +
                    "and should have at least one digit, one uppercase letter, " +
                    "one lowercase letter and one special character");
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
        newUser.setUsername(name);
        newUser.setEmail(email);
        newUser.setUsername(name);
        newUser.setHashedPassword(bcryptpasswordencoder.encode(password));
        newUser.setAddress(address);
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
