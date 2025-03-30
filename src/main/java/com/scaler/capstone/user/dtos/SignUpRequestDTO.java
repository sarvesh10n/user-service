package com.scaler.capstone.user.dtos;

import com.scaler.capstone.user.enums.Roles;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignUpRequestDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Street cannot be blank")
    private String street;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "State cannot be blank")
    private String state;

    @NotBlank(message = "ZipCode cannot be blank")
    private String zipcode;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    private List<Roles> roles;

    @NotBlank(message = "Reset Password Question cannot be blank")
    private String resetPasswordQuestion;

    @NotBlank(message = "Reset Password Answer cannot be blank")
    private String resetPasswordAnswer;
}
