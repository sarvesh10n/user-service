package com.scaler.capstone.user.dtos;

import com.scaler.capstone.user.models.Role;
import com.scaler.capstone.user.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String address;
    private List<String> roles;

    public static UserDTO fromUser(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());

        if(user.getAddress() != null){
            String addrs = user.getAddress().getStreet() +"," +
                    user.getAddress().getCity() + "," +
                    user.getAddress().getState() + "," +
                    user.getAddress().getCountry() + " - " +
                    user.getAddress().getZipcode();
            userDTO.setAddress(addrs);
        }

        if(user.getRoles() != null){
            List<String> roles = new ArrayList<>();
            for(Role role : user.getRoles())
            {
                roles.add(role.getName());
            }
            userDTO.setRoles(roles);
        }
        return userDTO;
    }
}
