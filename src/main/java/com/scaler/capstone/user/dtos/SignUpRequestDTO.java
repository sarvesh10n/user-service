package com.scaler.capstone.user.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDTO {
    private String name;
    private String email;
    private String password;
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String country;
}
