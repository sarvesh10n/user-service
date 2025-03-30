package com.scaler.capstone.user.services;

import com.scaler.capstone.user.enums.Roles;
import com.scaler.capstone.user.dtos.ResetPasswordDTO;
import com.scaler.capstone.user.dtos.SignUpRequestDTO;
import com.scaler.capstone.user.exception.InvalidDataException;
import com.scaler.capstone.user.exception.InvalidPasswordException;
import com.scaler.capstone.user.exception.UserAlreadyExistException;
import com.scaler.capstone.user.models.Address;
import com.scaler.capstone.user.models.Role;
import com.scaler.capstone.user.models.User;
import com.scaler.capstone.user.repositories.RoleRepository;
import com.scaler.capstone.user.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final String PASSWORD_REGEX =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$";

    private final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

    private BCryptPasswordEncoder bcryptpasswordencoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bcryptpasswordencoder,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bcryptpasswordencoder = bcryptpasswordencoder;
        this.roleRepository = roleRepository;
    }

    public User createUser(SignUpRequestDTO signUpRequestDTO) throws UserAlreadyExistException, InvalidPasswordException,InvalidDataException {

        String email = signUpRequestDTO.getEmail();

        String password = signUpRequestDTO.getPassword();
        String name = signUpRequestDTO.getName();
        String street = signUpRequestDTO.getStreet();
        String city  = signUpRequestDTO.getCity();
        String state = signUpRequestDTO.getState();
        String zip = signUpRequestDTO.getZipcode();
        String country = signUpRequestDTO.getCountry();
        List<Roles> roles = signUpRequestDTO.getRoles();


        if(!isValidPassword(password))
        {
            throw new InvalidPasswordException("Invalid password. It must be at least 8 characters long and include at least one digit," +
                    " one uppercase letter, one lowercase letter," +
                    " and one special character.");

        }

        List<Role> roleList = new ArrayList<>();
        if(!roles.isEmpty())
        {
            for(Roles role : roles)
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

        Optional<User> existingUser = userRepository.findByEmail(email);
        if(existingUser.isPresent()) {
            throw new UserAlreadyExistException("User with email id "+email+" already exists: ");
        }

        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setZipcode(zip);
        address.setCountry(country);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setHashedPassword(bcryptpasswordencoder.encode(password));
        user.setAddress(address);
        user.setRoles(roleList);
        user.setResetPasswordAnswer(signUpRequestDTO.getResetPasswordAnswer());
        user.setResetPasswordQuestion(signUpRequestDTO.getResetPasswordQuestion());
        return userRepository.save(user);
    }

    public User getUserByEmail(String email)  {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty())
        {
            throw new UsernameNotFoundException("User by email: " + email + " doesn't exist.");
        }
        return user.get();
    }



    public List<User> getAllUser()  {
        return userRepository.findAll();
    }

    public User resetPassword(ResetPasswordDTO resetPasswordDTO) throws InvalidDataException {
        Optional<User> existingUser = userRepository.findByEmail(resetPasswordDTO.getEmail());
        if(existingUser.isEmpty())
        {
            throw new UsernameNotFoundException("Email Id : " + resetPasswordDTO.getEmail()+ " is not present");
        }
        User user = existingUser.get();
        String actualResetPasswordQuestion = user.getResetPasswordQuestion();
        String actualResetPasswordAnswer = user.getResetPasswordAnswer();

        if(!resetPasswordDTO.getResetPasswordQuestion().equalsIgnoreCase(actualResetPasswordQuestion))
        {
            throw new InvalidDataException("Reset Password Question does not match.");
        }

        if(!resetPasswordDTO.getResetPasswordAnswer().equalsIgnoreCase(actualResetPasswordAnswer))
        {
            throw new InvalidDataException("Reset Password Answer does not match.");
        }

        String newEncodedPassword = bcryptpasswordencoder.encode(resetPasswordDTO.getNewPassword());
        user.setHashedPassword(newEncodedPassword);
        return userRepository.save(user);
    }

    public String getResetPasswordQuestion(String email) throws InvalidDataException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("Email Id : " + email + " doesn't exist.");
        }
        return user.get().getResetPasswordQuestion();
    }

    public User addRole(Long id, Roles roleName)
    {
        Optional<User> existingUser = userRepository.findById(id);
        if(existingUser.isEmpty())
        {
            throw new UsernameNotFoundException("Invalid UserId: " + id );
        }
        User user = existingUser.get();

        Role addRole;
        if(roleRepository.findByName(roleName).isPresent())
        {
            addRole = roleRepository.findByName(roleName).get();
        }
        else
        {
            addRole = new Role();
            addRole.setName(roleName);
            roleRepository.save(addRole);
        }
        user.getRoles().add(addRole);
        return userRepository.save(user);
    }

    public User removeRole(Long id, Roles roleName) throws InvalidDataException {
        Optional<User> existingUser = userRepository.findById(id);
        if(existingUser.isEmpty())
        {
            throw new UsernameNotFoundException("Invalid UserId: " + id );
        }
        User user = existingUser.get();

        Optional<Role> optionalRole = roleRepository.findByName(roleName);
        if(optionalRole.isEmpty())
        {
            throw new InvalidDataException("Role : " +roleName+" does not exist" );
        }
        user.getRoles().remove(optionalRole.get());
        return userRepository.save(user);
    }

    public User updateUser(Long id, Map<String, Object> updates)
    {
        Optional<User> existingUser = userRepository.findById(id);
        if(existingUser.isEmpty())
        {
            throw new UsernameNotFoundException("Invalid UserId: " + id );
        }

        User user = existingUser.get();
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    user.setName((String) value);
                    break;
                case "email":
                    user.setEmail((String) value);
                    break;
                case "hashedPassword":
                    user.setHashedPassword(bcryptpasswordencoder.encode((String) value));
                    break;
                case "resetPasswordQuestion":
                    user.setResetPasswordQuestion((String) value);
                    break;
                case "resetPasswordAnswer":
                    user.setResetPasswordAnswer((String) value);
                    break;
                case "street":
                    user.getAddress().setStreet((String) value);
                    break;
                case "city":
                    user.getAddress().setCity((String) value);
                    break;
                case "state":
                    user.getAddress().setState((String) value);
                    break;
                case "zipcode":
                    user.getAddress().setZipcode((String) value);
                    break;
                case "country":
                    user.getAddress().setCountry((String) value);
                    break;
            }
        });
        return userRepository.save(user);
    }

    public void deleteUser(String email){
        Optional<User> existingUser = userRepository.findByEmail(email);
        if(existingUser.isEmpty()){
            throw new UsernameNotFoundException("User by email: " + email + " doesn't exist.");
        }
        User user = existingUser.get();
        user.getRoles().removeAll(user.getRoles());
        userRepository.delete(user);
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
